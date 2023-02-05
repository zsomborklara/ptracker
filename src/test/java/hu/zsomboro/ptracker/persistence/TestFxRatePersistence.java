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

import hu.zsomboro.ptracker.persistence.entity.FxRateId;
import hu.zsomboro.ptracker.persistence.entity.HufFxRateDO;

@DataJpaTest
@ContextConfiguration
public class TestFxRatePersistence {

  @Autowired
  private HufFxRateDORepository fxRateRepo;

  @Test
  public void testFxRate() {
    HufFxRateDO fxRate = new HufFxRateDO();
    LocalDate asOfDate = LocalDate.now();
    String identifier = "USD";

    fxRate.setAsOfDate(asOfDate);
    fxRate.setIsoCurrency(identifier);
    fxRate.setValue(0.2233d);

    fxRateRepo.save(fxRate);

    FxRateId id = new FxRateId();
    id.setAsOfDate(asOfDate);
    id.setIsoCurrency(identifier);
    Optional<HufFxRateDO> foundFxRateOp = fxRateRepo.findById(id);

    assertTrue(foundFxRateOp.isPresent());
    HufFxRateDO foundFxRate = foundFxRateOp.get();
    assertThat(fxRate.getAsOfDate(), equalTo(foundFxRate.getAsOfDate()));
    assertThat(fxRate.getIsoCurrency(), equalTo(foundFxRate.getIsoCurrency()));
    assertThat(fxRate.getValue(), closeTo(foundFxRate.getValue(), 1e-14));
  }

  @Test
  public void testFindAllPricesByDate() {
    LocalDate asOfDate = LocalDate.now();
    String identifier1 = "USD";
    HufFxRateDO fxrate1 = new HufFxRateDO();

    fxrate1.setAsOfDate(asOfDate);
    fxrate1.setIsoCurrency(identifier1);
    fxrate1.setValue(12.55d);

    String identifier2 = "GBP";
    HufFxRateDO fxrate2 = new HufFxRateDO();

    fxrate2.setAsOfDate(asOfDate);
    fxrate2.setIsoCurrency(identifier2);
    fxrate2.setValue(12.55d);

    String identifier3 = "EUR";
    HufFxRateDO fxrate3 = new HufFxRateDO();

    fxrate3.setAsOfDate(asOfDate.minusDays(1));
    fxrate3.setIsoCurrency(identifier3);
    fxrate3.setValue(12.55d);

    fxRateRepo.saveAll(List.of(fxrate1, fxrate2, fxrate3));

    List<HufFxRateDO> foundPrices = fxRateRepo.findByAsOfDate(LocalDate.now());

    assertThat(foundPrices, containsInAnyOrder(fxrate1, fxrate2));

  }

}
