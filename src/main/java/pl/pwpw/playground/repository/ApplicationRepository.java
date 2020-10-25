package pl.pwpw.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pwpw.playground.model.application.Application;
import pl.pwpw.playground.model.application.ApplicationNumber;
import pl.pwpw.playground.model.application.EmailAddress;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

  Application findByApplicationNumber(ApplicationNumber applicationNumber);

  @Query("SELECT appl FROM Application appl WHERE appl.contactDetails.emailAddress.emailAddress = :emailAddress")
  Application findByEmailAddress(@Param("emailAddress") String emailAddress);
}
