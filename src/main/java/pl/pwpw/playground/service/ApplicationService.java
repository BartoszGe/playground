package pl.pwpw.playground.service;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.pwpw.playground.model.application.Application;
import pl.pwpw.playground.model.application.ApplicationNumber;
import pl.pwpw.playground.model.application.ContactDetails;
import pl.pwpw.playground.model.application.EmailAddress;
import pl.pwpw.playground.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final ApplicationRepository applicationRepository;

  public ContactDetails findContact(final String applicationNumber) {

    return Try.of(() -> applicationRepository.findByApplicationNumber(new ApplicationNumber(applicationNumber)))
              .mapTry(Application::getContactDetails)
              .getOrNull();
  }

  public Application findApplication(final String email) {

    return Try.of(() -> applicationRepository.findByEmailAddress(email))
              .getOrNull();
  }
}
