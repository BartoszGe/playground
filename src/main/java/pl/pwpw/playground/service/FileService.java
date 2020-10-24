package pl.pwpw.playground.service;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.PlaygroundProperties;
import pl.pwpw.playground.model.ApplicationFile;
import pl.pwpw.playground.model.ApplicationFileType;
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

  @Autowired
  public FileService(PlaygroundProperties properties,
                     FileRepository fileRepository) {

    this.rootLocation = Paths.get(properties.getLocation());
    this.fileRepository = fileRepository;

    init();
  }

  public void init() {

    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new FileException("Could not initialize storage", e);
    }
  }

  public URI save(final MultipartFile file, final String applicationId) {

    Option.of(file.isEmpty())
          .getOrElseThrow(() -> new FileException("Failed to save an empty file"));

    String fileName = Option.of(file.getOriginalFilename())
                            .map(StringUtils::cleanPath)
                            .getOrElseThrow(() -> new FileException("File name cannot be null"));

    ApplicationFileType fileType = Option.of(ApplicationFileType.valueOf(getFileType(fileName).toUpperCase()))
                                         .getOrElseThrow(() -> new FileException("Wrong file format. Possible: pdf or jpg"));

    URI newFileUri = this.rootLocation.resolve(fileName).toUri();

    Try.of(file::getInputStream)
       .andThenTry(inputStream -> copyFile(inputStream, fileName))
       .onSuccess(info -> log.info("Successful copying of the file: {}", info))
       .getOrElseThrow(throwable -> new FileException("Cannot copy a file", throwable));

    saveToDatabase(applicationId, newFileUri, fileType);

    return newFileUri;
  }

  private void copyFile(final InputStream inputStream, final String filename) {

    Try.of(() -> Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING))
       .getOrElseThrow(() -> new FileException("Problem with copying the file"));
  }

  private void saveToDatabase(final String applicationId, final URI newFileUri, final ApplicationFileType applicationFileType) {

    BasicFileAttributes fileAttributes = Try.of(() -> Files.readAttributes(Path.of(newFileUri), BasicFileAttributes.class))
                                            .getOrElseThrow(() -> new FileException("Problem with reading the file"));

    Date creationDate = new Date(fileAttributes.creationTime().toMillis());
    ApplicationFileType fileType = applicationFileType;

    fileRepository.save(new ApplicationFile(null, creationDate, fileType, newFileUri.toString()));
  }

  private String getFileType(final String fileName) {

    return List.of(fileName.split("\\.")).last();
  }
}
