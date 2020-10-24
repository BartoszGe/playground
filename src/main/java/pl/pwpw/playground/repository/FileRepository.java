package pl.pwpw.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pwpw.playground.model.ApplicationFile;

@Repository
public interface FileRepository extends JpaRepository<ApplicationFile, Long> {

}
