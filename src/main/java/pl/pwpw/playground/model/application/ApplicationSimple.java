package pl.pwpw.playground.model.application;

import lombok.Value;

@Value
public class ApplicationSimple {

  ApplicationNumber applicationNumber;

  String firstName;

  ApplicationType applicationType;

  public static ApplicationSimple createApplicationSimpleFrom(Application application) {

    return new ApplicationSimple(application.getApplicationNumber(), application.getFirstName(), application.getApplicationType());
  }
}
