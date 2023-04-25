package hu.zsomboro.ptracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "hu.zsomboro.ptracker.*" })
@EnableJpaRepositories("hu.zsomboro.ptracker.persistence")
@EntityScan("hu.zsomboro.ptracker.persistence.entity")
public class PortfolioTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioTrackerApplication.class);
  }

}
