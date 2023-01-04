package hu.zsommboro.ptracker.integration.contract;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import io.restassured.RestAssured;

@AutoConfigureTestDatabase
@ContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PortfolioControllerBase {

  @LocalServerPort
  private int port;

  @BeforeEach
  public void setup() {

    RestAssured.baseURI = "http://localhost";
    RestAssured.port = this.port;
  }

  @Configuration
  @ComponentScan(basePackages = { "hu.zsomboro" })
  public static class SpringIntegrationTestConfig {

  }
}
