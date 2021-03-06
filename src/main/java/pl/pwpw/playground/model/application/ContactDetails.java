package pl.pwpw.playground.model.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 *
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails {

  @Embedded
  private EmailAddress emailAddress;

  @Embedded
  private PhoneNumber phoneNumber;
}
