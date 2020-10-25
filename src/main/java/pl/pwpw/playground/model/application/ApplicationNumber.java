package pl.pwpw.playground.model.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationNumber {

  private String applicationNumber;
}
