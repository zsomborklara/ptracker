package hu.zsomboro.ptracker.service;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration
public class FxRateServiceImplTest {

  @Autowired
  private FxRateService fxRateService;

  @Test
  public void testSaveFxRate() {

    String iso = "EUR";
    fxRateService.saveHufFxRate(LocalDate.now(), iso, 300.d);
    double hufFxRate = fxRateService.getHufFxRate(LocalDate.now(), iso);

    assertThat(hufFxRate).isCloseTo(300.d, Offset.offset(1e-10));
  }

  @Test
  public void testFindAllSavedFxRatesForDay() {

   fxRateService.saveHufFxRate(LocalDate.now(), "EUR", 300.d);
    fxRateService.saveHufFxRate(LocalDate.now(), "USD", 290.d);
    fxRateService.saveHufFxRate(LocalDate.now().plusDays(1), "PLN", 55.d);
    Map<String, Double> fxRatesForDay = fxRateService.getAllFxRatesForDay(LocalDate.now());

    assertThat(fxRatesForDay).hasSize(2).containsKeys("EUR", "USD").containsValues(300.d, 290.d);
  }

  @Configuration
  @EnableAutoConfiguration
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.common", "hu.zsomboro.ptracker.service",
      "hu.zsomboro.ptracker.persistence" })
  public static class SpringServiceTestConfig {

  }

}
