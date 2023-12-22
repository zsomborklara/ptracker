package hu.zsomboro.ptracker.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;
import jakarta.persistence.PersistenceException;

@DataJpaTest
@ContextConfiguration
public class TestPortfolioPersistence {

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private PortfolioDORepository portfolioRepo;
  @Autowired
  private InstrumentDORepository instrumentRepo;

  private final InstrumentDO i1 = new EquitySecurityDO("inst1", "INST1", "EXCHANGE_TRADED_FUND");
  private final InstrumentDO i2 = new FixedIncomeSecurityDO("inst2", "INST2", "STOCK", LocalDate.now(), 2.3d);

  @Test
  public void testSavePortfolio() {

    PortfolioDO portfolio = getPortfolioDO();
    portfolioRepo.save(portfolio);
    PortfolioDO newPortfolio = portfolioRepo.findById(portfolio.getId());
    assertThat(newPortfolio, notNullValue());
    assertThat(newPortfolio.getId(), greaterThan(0L));
    assertThat(portfolio.getId(), equalTo(newPortfolio.getId()));
    assertThat(portfolio.getCash(), equalTo(newPortfolio.getCash()));
    Set<ConstituentDO> constituents = portfolio.getConstituents();
    Set<ConstituentDO> newConstituents = newPortfolio.getConstituents();
    for (ConstituentDO cdo : constituents) {
      boolean found = false;
      for (ConstituentDO newCdo : newConstituents) {
        if (cdo.getId().equals(newCdo.getId())) {
          found = true;
          assertThat(cdo.getInstrument().getName(), equalTo(newCdo.getInstrument().getName()));
          assertThat(cdo.getInstrument().getInstrumentId(), equalTo(newCdo.getInstrument().getInstrumentId()));
          assertThat(cdo.getInstrument().getInstrumentType(), equalTo(newCdo.getInstrument().getInstrumentType()));
          assertThat(cdo.getId(), equalTo(newCdo.getId()));
          assertThat(newCdo.getId().getPortfolioId(), equalTo(newPortfolio.getId()));
          assertThat(cdo.getNumber(), equalTo(newCdo.getNumber()));
          if (newCdo.getInstrument() instanceof FixedIncomeSecurityDO) {
            assertThat(((FixedIncomeSecurityDO) cdo.getInstrument()).getInterestRate(),
                    closeTo(((FixedIncomeSecurityDO) newCdo.getInstrument()).getInterestRate(), 1e-15));
            assertThat(((FixedIncomeSecurityDO) cdo.getInstrument()).getMaturity(),
                    equalTo(((FixedIncomeSecurityDO) newCdo.getInstrument()).getMaturity()));
          }
        }
      }
      assertThat(found, equalTo(true));
    }
  }

  @Test
  public void testFindPortfolioByName() {

    PortfolioDO portfolio = getPortfolioDO();
    portfolioRepo.save(portfolio);
    List<PortfolioDO> newPortfolio = portfolioRepo.findByName(portfolio.getName());
    assertThat(newPortfolio, hasSize(1));
    assertThat(newPortfolio.get(0).getName(), equalTo("Dummy"));
  }

  @Test
  public void testPortfolioNameIsUnique() {

    PortfolioDO portfolio1 = getPortfolioDO();
    entityManager.persistAndFlush(portfolio1);
    Assertions.assertThrows(PersistenceException.class, () -> {
      PortfolioDO portfolio2 = getPortfolioDO();
      entityManager.persistAndFlush(portfolio2);
    });
  }

  @Test
  public void testGetStockInstruments() {

    PortfolioDO portfolio = getPortfolioDO();
    portfolioRepo.save(portfolio);
    final Collection<InstrumentDO> existingStocks = instrumentRepo
        .findByInstrumentType(InstrumentType.STOCK.toString());
    assertThat(existingStocks, hasSize(1));
    InstrumentDO stockInstrument = existingStocks.iterator().next();
    assertThat("STOCK", equalTo(stockInstrument.getInstrumentType()));
    assertThat("INST2", equalTo(stockInstrument.getInstrumentId()));
    assertThat("inst2", equalTo(stockInstrument.getName()));

  }

  private PortfolioDO getPortfolioDO() {
    CashDO usd = new CashDO("USD", 100.d);
    PortfolioDO portfolio = new PortfolioDO("Dummy");
    ConstituentDO cdo1 = new ConstituentDO(i1, 10, portfolio);
    ConstituentDO cdo2 = new ConstituentDO(i2, 20, portfolio);
    portfolio.setCash(usd);
    portfolio.setConstituents(Set.of(cdo1, cdo2));
    return portfolio;
  }
}
