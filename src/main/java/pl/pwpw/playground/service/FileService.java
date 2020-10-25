package pl.pwpw.playground.service;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.PlaygroundProperties;
import pl.pwpw.playground.model.application.Application;
import pl.pwpw.playground.model.file.ApplicationFile;
import pl.pwpw.playground.model.file.ApplicationFileType;
import pl.pwpw.playground.repository.ApplicationRepository;
import pl.pwpw.playground.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

@Slf4j
@Service
public class FileService {

  private final Path rootLocation;

  private final FileRepository fileRepository;

  private final ApplicationRepository applicationRepository;

  @Autowired
  public FileService(PlaygroundProperties properties,
                     FileRepository fileRepository,
                     ApplicationRepository applicationRepository) {

    this.rootLocation = Paths.get(properties.getLocation());
    this.fileRepository = fileRepository;
    this.applicationRepository = applicationRepository;

    init();
  }

  public void init() {

    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new FileException("Could not initialize storage", e);
    }
  }

  public URI save(final MultipartFile file, final Long applicationId) {

    String fileName = Option.of(file.getOriginalFilename())
                            .map(StringUtils::cleanPath)
                            .getOrElseThrow(() ->new FileException("File name cannot be blank"));

    ApplicationFileType fileType = Try.of(() -> ApplicationFileType.valueOf(getFileType(fileName).toUpperCase()))
                                         .getOrElseThrow(() -> new FileException("Wrong file format. Possible: pdf or jpg"));

    Application application = Try.of(() -> applicationRepository.findById(applicationId).get())
                                 .getOrElseThrow(() -> new FileException("Failed to find application using this id"));

    Try.of(file::getInputStream)
       .andThenTry(inputStream -> copyFile(inputStream, fileName))
       .onSuccess(info -> log.info("Successful copying of the file: {}", info))
       .getOrElseThrow(throwable -> new FileException("Cannot copy a file", throwable));

    URI newFileUri = this.rootLocation.resolve(fileName).toUri();
    saveToDatabase(newFileUri, fileType, application);
    return newFileUri;
  }

  private void copyFile(final InputStream inputStream, final String filename) {

    Try.run(() -> Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING))
       .getOrElseThrow(() -> new FileException("Problem with copying the file"));
  }

  private void saveToDatabase(final URI newFileUri, final ApplicationFileType applicationFileType,
                              final Application application) {

    BasicFileAttributes fileAttributes = Try.of(() -> Files.readAttributes(Path.of(newFileUri), BasicFileAttributes.class))
                                            .getOrElseThrow(() -> new FileException("Problem with reading attributes from file"));

    Date creationDate = new Date(fileAttributes.creationTime().toMillis());

    fileRepository.save(new ApplicationFile(null, creationDate, applicationFileType, newFileUri.toString(), application));
  }

  private String getFileType(final String fileName) {

    return List.of(fileName.split("\\.")).last();
  }
}
