package pl.pwpw.playground.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;

@Data
@Entity
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFile implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Date date;

  @Enumerated(EnumType.STRING)
  private ApplicationFileType type;

  private String filePath;
}
