package hu.zsomboro.persistence;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class TestPersistenceHelperDAO {

  private PortfolioDO portfolio;
  private PersistenceHelperDAO dao;

  @Before
  public void init() {
    InstrumentDO i1 = new InstrumentDO("inst1", "INST1", "ISIN");
    InstrumentDO i2 = new InstrumentDO("inst2", "INST2", "ISIN");

    ConstituentDO cdo1 = new ConstituentDO(i1, 10);
    ConstituentDO cdo2 = new ConstituentDO(i2, 20);

    portfolio = new PortfolioDO(Sets.<ConstituentDO>newHashSet(cdo1, cdo2));
    dao = new PersistenceHelperImpl();
  }

  @Test
  public void testSavePortfolio() {
    dao.savePortfolio(portfolio);
    PortfolioDO newPortfolio = dao.findPortfolio(1);
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
          Assert.assertEquals(cdo.getNumber(), newCdo.getNumber());
        }
      }
      Assert.assertTrue(found);
    }
  }
}
