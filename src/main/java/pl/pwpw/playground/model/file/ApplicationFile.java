package pl.pwpw.playground.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwpw.playground.model.application.Application;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
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

  @OneToOne
  private Application application;
}
