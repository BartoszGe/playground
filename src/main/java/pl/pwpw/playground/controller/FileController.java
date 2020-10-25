package pl.pwpw.playground.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pwpw.playground.service.ApplicationService;
import pl.pwpw.playground.service.FileService;


@RestController
@RequestMapping(path = "file")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PostMapping("/")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                      @RequestParam("application_id") String applicationId) {

    return ResponseEntity.created(fileService.save(file, Long.valueOf(applicationId))).build();
  }
}
