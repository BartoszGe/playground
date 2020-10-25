package pl.pwpw.playground.model.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationNumber {

  private String applicationNumber;
}
