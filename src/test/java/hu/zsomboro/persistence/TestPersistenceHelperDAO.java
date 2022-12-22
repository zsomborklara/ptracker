package hu.zsomboro.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Sets;

import hu.zsomboro.core.Cash;
import hu.zsomboro.persistence.entity.CashDO;
import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.EquitySecurityDO;
import hu.zsomboro.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.persistence.entity.InstrumentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

@DataJpaTest
@ContextConfiguration
public class TestPersistenceHelperDAO {

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private PersistenceHelperService service;

  private InstrumentDO i1 = new EquitySecurityDO("inst1", "INST1", "EXCHANGE_TRADED_FUND");
  private InstrumentDO i2 = new FixedIncomeSecurityDO("inst2", "INST2", "STOCK", LocalDate.now(), 2.3d);

  private ConstituentDO cdo1 = new ConstituentDO(i1, 10);
  private ConstituentDO cdo2 = new ConstituentDO(i2, 20);

  @Test
  public void testSavePortfolio() {

    PortfolioDO portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), new CashDO("USD", 100.d));
    entityManager.persistAndFlush(portfolio);
    PortfolioDO newPortfolio = service.findPortfolio(portfolio.getId());
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
  public void testGetStockInstruments() {

    PortfolioDO portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2), Cash.ZERO.toDataObject());
    entityManager.persist(portfolio);
    final Collection<InstrumentDO> existingStocks = service.getAllStockInstruments();
    assertEquals(1, existingStocks.size());
    InstrumentDO stockInstrument = existingStocks.iterator().next();
    assertEquals("STOCK", stockInstrument.getInstrumentType());
    assertEquals("INST2", stockInstrument.getIdentifier());
    assertEquals("inst2", stockInstrument.getName());

  }

  @Configuration
  @ComponentScan(basePackages = { "hu.zsomboro" })
  public static class SpringConfig {

  }
}
