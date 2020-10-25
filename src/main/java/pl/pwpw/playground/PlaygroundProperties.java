package pl.pwpw.playground;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("playground")
public class PlaygroundProperties {

  private String location = "attachments";
}
