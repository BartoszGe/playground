package pl.pwpw.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pwpw.playground.model.application.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
