package hu.zsomboro.persistence;

import com.google.common.collect.Sets;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

public class TestPersistenceHelperDAO {

  private static PortfolioDO portfolio;
  private static PersistenceHelperDAO dao;

  @BeforeClass
  public static void init() {
    InstrumentDO i1 = new InstrumentDO("inst1", "INST1", "ISIN", "MUTUAL_FUND");
    InstrumentDO i2 = new InstrumentDO("inst2", "INST2", "ISIN", "STOCK");

    ConstituentDO cdo1 = new ConstituentDO(i1, 10);
    ConstituentDO cdo2 = new ConstituentDO(i2, 20);

    portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2));
    dao = new PersistenceHelperImpl();
    dao.savePortfolio(portfolio);
  }

  @AfterClass
  public static void tearDown(){
    try {
      dao.removePortfolio(portfolio);
    }catch (Exception e) {
      //ignored
    }
  }

  @Test
  public void testSavePortfolio() {
    PortfolioDO newPortfolio = dao.findPortfolio(portfolio.getId());
    Assert.assertNotNull(newPortfolio);
    Assert.assertEquals(portfolio.getId(), newPortfolio.getId());
    Set<ConstituentDO> constituents = portfolio.getConstituents();
    Set<ConstituentDO> newConstituents = newPortfolio.getConstituents();
    for (ConstituentDO cdo : constituents) {
      boolean found = false;
      for (ConstituentDO newCdo : newConstituents) {
        if (cdo.getId() == newCdo.getId()) {
          found = true;
          Assert.assertEquals(cdo.getInstrument().getName(), newCdo.getInstrument().getName());
          Assert.assertEquals(cdo.getInstrument().getIdentifier(), newCdo.getInstrument().getIdentifier());
          Assert.assertEquals(cdo.getInstrument().getIdType(), newCdo.getInstrument().getIdType());
          Assert.assertEquals(cdo.getInstrument().getInstrumentType(), newCdo.getInstrument().getInstrumentType());
          Assert.assertEquals(cdo.getNumber(), newCdo.getNumber());
        }
      }
      Assert.assertTrue(found);
    }
  }

  @Test
  public void testGetStockInstruments() {

    final Collection<InstrumentDO> existingStocks = dao.getAllStockInstruments();
    Assert.assertEquals(1, existingStocks.size());
    InstrumentDO stockInstrument = existingStocks.iterator().next();
    Assert.assertEquals("STOCK", stockInstrument.getInstrumentType());
    Assert.assertEquals("INST2", stockInstrument.getIdentifier());
    Assert.assertEquals("ISIN", stockInstrument.getIdType());
    Assert.assertEquals("inst2", stockInstrument.getName());

  }
}
