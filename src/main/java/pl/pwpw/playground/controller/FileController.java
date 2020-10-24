package pl.pwpw.playground.controller;

import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.service.FileService;

import java.net.URI;

@Controller
@RequestMapping(path = "file")
public class FileController {

  private final FileService fileService;

  @Autowired
  FileController(FileService fileService) {

    this.fileService = fileService;
  }

  @PostMapping("/")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                      @RequestParam("application_id") String applicationId) {

    return ResponseEntity.created(fileService.save(file, applicationId)).build();
  }
}
