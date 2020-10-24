package pl.pwpw.playground.service;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.PlaygroundProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static pl.pwpw.playground.service.FileException.causedBy;

@Slf4j
@Service
public class FileService {

  private final Path rootLocation;

  @Autowired
  public FileService(PlaygroundProperties properties) {

    this.rootLocation = Paths.get(properties.getLocation());
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

    String filename = Option.of(file.getOriginalFilename())
                            .map(StringUtils::cleanPath)
                            .getOrElseThrow(() -> new FileException("File name cannot be null"));

    Try.of(file::getInputStream)
       .andThenTry(inputStream -> copyFile(inputStream, filename))
       .onSuccess(info -> log.info("Successful copying of the file: {}", info))
       .getOrElseThrow(throwable -> new FileException("Cannot copy a file", throwable));

    return this.rootLocation.resolve(filename).toUri();
  }

  private void copyFile(final InputStream inputStream, final String filename) {

    Try.of(() -> Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING))
       .getOrElseThrow(() -> new FileException("Problems with copying the file"));
  }

  public Path load(String filename) {

    return rootLocation.resolve(filename);
  }
}
