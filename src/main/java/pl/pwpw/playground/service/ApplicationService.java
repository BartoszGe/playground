package pl.pwpw.playground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pwpw.playground.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final ApplicationRepository applicationRepository;

  public boolean existsById(final Long valueOf) {

    return applicationRepository.existsById(valueOf);
  }
}
