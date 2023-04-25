package hu.zsomboro.ptracker.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.google.common.collect.Sets;

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

  private InstrumentDO i1 = new EquitySecurityDO("inst1", "INST1", "EXCHANGE_TRADED_FUND");
  private InstrumentDO i2 = new FixedIncomeSecurityDO("inst2", "INST2", "STOCK", LocalDate.now(), 2.3d);

  private ConstituentDO cdo1 = new ConstituentDO(i1, 10);
  private ConstituentDO cdo2 = new ConstituentDO(i2, 20);

  @Test
  public void testSavePortfolio() {

    PortfolioDO portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("USD", 100.d),
        "Dummy");
    portfolioRepo.save(portfolio);
    PortfolioDO newPortfolio = portfolioRepo.findById(portfolio.getId());
    assertNotNull(newPortfolio);
    assertEquals(portfolio.getId(), newPortfolio.getId());
    assertEquals(portfolio.getCash(), newPortfolio.getCash());
    Set<ConstituentDO> constituents = portfolio.getConstituents();
    Set<ConstituentDO> newConstituents = newPortfolio.getConstituents();
    for (ConstituentDO cdo : constituents) {
      boolean found = false;
      for (ConstituentDO newCdo : newConstituents) {
        if (cdo.getId() == newCdo.getId()) {
          found = true;
          assertEquals(cdo.getInstrument().getName(), newCdo.getInstrument().getName());
          assertEquals(cdo.getInstrument().getIdentifier(), newCdo.getInstrument().getIdentifier());
          assertEquals(cdo.getInstrument().getInstrumentType(), newCdo.getInstrument().getInstrumentType());
          assertEquals(cdo.getNumber(), newCdo.getNumber());
          if (newCdo.getInstrument() instanceof FixedIncomeSecurityDO) {
            assertEquals(((FixedIncomeSecurityDO) cdo.getInstrument()).getInterestRate(),
                ((FixedIncomeSecurityDO) newCdo.getInstrument()).getInterestRate(), 1e-15);
            assertEquals(((FixedIncomeSecurityDO) cdo.getInstrument()).getMaturity(),
                ((FixedIncomeSecurityDO) newCdo.getInstrument()).getMaturity());
          }
        }
      }
      assertTrue(found);
    }
  }

  @Test
  public void testFindPortfolioByName() {

    PortfolioDO portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("USD", 100.d),
        "Dummy");
    portfolioRepo.save(portfolio);
    List<PortfolioDO> newPortfolio = portfolioRepo.findByName(portfolio.getName());
    assertThat(newPortfolio, hasSize(1));
    assertThat(newPortfolio.get(0).getName(), equalTo("Dummy"));
  }

  @Test
  public void testPortfolioNameIsUnique() {

    PortfolioDO portfolio1 = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("USD", 100.d),
        "Dummy");
    entityManager.persistAndFlush(portfolio1);
    Assertions.assertThrows(PersistenceException.class, () -> {
      PortfolioDO portfolio2 = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("USD", 100.d),
          "Dummy");
      entityManager.persistAndFlush(portfolio2);
    });

  }

  @Test
  public void testGetStockInstruments() {

    PortfolioDO portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("HUF", 0.d),
        "Dummy");
    portfolioRepo.save(portfolio);
    final Collection<InstrumentDO> existingStocks = instrumentRepo
        .findByInstrumentType(InstrumentType.STOCK.toString());
    assertEquals(1, existingStocks.size());
    InstrumentDO stockInstrument = existingStocks.iterator().next();
    assertEquals("STOCK", stockInstrument.getInstrumentType());
    assertEquals("INST2", stockInstrument.getIdentifier());
    assertEquals("inst2", stockInstrument.getName());

  }
}
