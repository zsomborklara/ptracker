package hu.zsomboro.ptracker.service;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.persistence.PriceDORepository;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
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
public class LoadedPriceServiceImplTest {

  @Autowired
  private LoadedPriceService loadedPriceService;

  @Autowired
  private PriceDORepository priceDORepository;

  @BeforeEach
  public void init() {
    insertPrice(10.d, "DUMMY1", LocalDate.now(), "HUF");
    insertPrice(13.d, "DUMMY2", LocalDate.now(), "EUR");
    insertPrice(22.1, "DUMMY2", LocalDate.now().plusDays(1), "EUR");
  }

  @Test
  public void testFindPriceAsOfDate() {

    Price price = loadedPriceService.getPrice(LocalDate.now(), () -> "DUMMY1");

    assertThat(price.value()).isCloseTo(10.d, Offset.offset(1e-12));
    assertThat(price.priceCurrency()).isEqualTo("HUF");
  }

  @Test
  public void testFindPriceHistory() {

    LocalDate firstDay = LocalDate.now();
    LocalDate secondDay = LocalDate.now().plusDays(1);
    Map<LocalDate, Price> priceHistory = loadedPriceService.getPriceHistory(() -> "DUMMY2");

    assertThat(priceHistory).hasSize(2).containsKeys(firstDay, secondDay);

    assertThat(priceHistory.get(firstDay).priceCurrency()).isEqualTo("EUR");
    assertThat(priceHistory.get(secondDay).priceCurrency()).isEqualTo("EUR");
    assertThat(priceHistory.get(firstDay).value()).isCloseTo(13.d, Offset.offset(1e-14));
    assertThat(priceHistory.get(secondDay).value()).isCloseTo(22.1d, Offset.offset(1e-14));
  }

  @Test
  public void testFindAllPricesForDay() {

    LocalDate today = LocalDate.now();
    Map<String, Price> prices = loadedPriceService.getAllPricesForDay(today);

    assertThat(prices).hasSize(2).containsKeys("DUMMY1", "DUMMY2");

    assertThat(prices.get("DUMMY1").priceCurrency()).isEqualTo("HUF");
    assertThat(prices.get("DUMMY2").priceCurrency()).isEqualTo("EUR");
    assertThat(prices.get("DUMMY1").value()).isCloseTo(10.d, Offset.offset(1e-14));
    assertThat(prices.get("DUMMY2").value()).isCloseTo(13.d, Offset.offset(1e-14));
  }

  private void insertPrice(double price, String id, LocalDate asOf, String currency) {
    PriceDO price1 = new PriceDO();
    price1.setPrice(price);
    price1.setIdentifier(id);
    price1.setAsOfDate(asOf);
    price1.setCurrency(currency);
    priceDORepository.save(price1);
  }


  @Configuration
  @EnableAutoConfiguration
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.common", "hu.zsomboro.ptracker.service",
      "hu.zsomboro.ptracker.persistence" })
  public static class SpringServiceTestConfig {

  }

}
