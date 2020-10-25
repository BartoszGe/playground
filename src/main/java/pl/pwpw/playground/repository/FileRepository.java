package pl.pwpw.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pwpw.playground.model.file.ApplicationFile;

@Repository
public interface FileRepository extends JpaRepository<ApplicationFile, Long> {

}
