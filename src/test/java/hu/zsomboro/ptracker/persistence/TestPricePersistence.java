package hu.zsomboro.ptracker.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import hu.zsomboro.ptracker.persistence.entity.PriceId;

@DataJpaTest
@ContextConfiguration
public class TestPricePersistence {

  @Autowired
  private PriceDORepository priceRepo;

  @Test
  public void testSavePrice() {
    PriceDO price = new PriceDO();
    LocalDate asOfDate = LocalDate.now();
    String identifier = "abc123";

    price.setAsOfDate(asOfDate);
    price.setCurrency("HUF");
    price.setIdentifier(identifier);
    price.setPrice(12.55d);

    priceRepo.save(price);

    PriceId id = new PriceId();
    id.setAsOfDate(asOfDate);
    id.setIdentifier(identifier);
    Optional<PriceDO> foundPriceOp = priceRepo.findById(id);

    assertTrue(foundPriceOp.isPresent());
    PriceDO foundPrice = foundPriceOp.get();
    assertThat(price.getAsOfDate(), equalTo(foundPrice.getAsOfDate()));
    assertThat(price.getCurrency(), equalTo(foundPrice.getCurrency()));
    assertThat(price.getIdentifier(), equalTo(foundPrice.getIdentifier()));
    assertThat(price.getPrice(), closeTo(foundPrice.getPrice(), 1e-14));
  }

  @Test
  public void testFindAllPricesByDate() {
    LocalDate asOfDate = LocalDate.now();
    String identifier1 = "abc123";
    PriceDO price1 = new PriceDO();

    price1.setAsOfDate(asOfDate);
    price1.setCurrency("HUF");
    price1.setIdentifier(identifier1);
    price1.setPrice(12.55d);

    String identifier2 = "abc124";
    PriceDO price2 = new PriceDO();

    price2.setAsOfDate(asOfDate);
    price2.setCurrency("HUF");
    price2.setIdentifier(identifier2);
    price2.setPrice(12.55d);

    String identifier3 = "abc125";
    PriceDO price3 = new PriceDO();

    price3.setAsOfDate(asOfDate.minusDays(1));
    price3.setCurrency("HUF");
    price3.setIdentifier(identifier3);
    price3.setPrice(12.55d);

    priceRepo.saveAll(List.of(price1, price2, price3));

    List<PriceDO> foundPrices = priceRepo.findByAsOfDate(LocalDate.now());

    assertThat(foundPrices, containsInAnyOrder(price1, price2));

  }

  @Test
  public void testFindPriceHistory() {
    LocalDate asOfDate1 = LocalDate.now();
    String identifier = "abc123";
    PriceDO price1 = new PriceDO();

    price1.setAsOfDate(asOfDate1);
    price1.setCurrency("HUF");
    price1.setIdentifier(identifier);
    price1.setPrice(12.55d);

    PriceDO price2 = new PriceDO();

    LocalDate asOfDate2 = LocalDate.now().minusDays(1);
    price2.setAsOfDate(asOfDate2);
    price2.setCurrency("HUF");
    price2.setIdentifier(identifier);
    price2.setPrice(12.55d);

    PriceDO price3 = new PriceDO();
    LocalDate asOfDate3 = LocalDate.now().minusDays(2);
    price3.setAsOfDate(asOfDate3);
    price3.setCurrency("HUF");
    price3.setIdentifier("dummy");
    price3.setPrice(12.55d);

    priceRepo.saveAll(List.of(price1, price2));

    List<PriceDO> foundPrices = priceRepo.findByIdentifier(identifier);

    assertThat(foundPrices, containsInAnyOrder(price1, price2));

  }

}
