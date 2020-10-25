package pl.pwpw.playground.controller;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pwpw.playground.model.application.ContactDetails;
import pl.pwpw.playground.service.ApplicationService;

@RestController
@RequestMapping(path = "application")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  @GetMapping("/contact")
  public ResponseEntity<?> findContact(@RequestParam("application_number") String applicationNumber) {

    ContactDetails contactDetails = applicationService.findContact(applicationNumber);
    if (Option.of(contactDetails).isDefined()) {
      return ResponseEntity.ok(contactDetails);
    }
    return ResponseEntity.notFound().build();
  }
}
