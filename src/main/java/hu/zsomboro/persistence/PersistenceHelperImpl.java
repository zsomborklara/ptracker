package hu.zsomboro.persistence;

import hu.zsomboro.common.Constants;
import hu.zsomboro.core.InstrumentType;
import hu.zsomboro.core.Portfolio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PersistenceHelperImpl implements PersistenceHelperDAO {

  private static final Logger LOG = LogManager.getLogger(PersistenceHelperImpl.class);
  private final PersistenceManagerFactory pmf;

  public PersistenceHelperImpl(PersistenceManagerFactory pmf) {
    this.pmf = pmf;
  }

  public PersistenceHelperImpl(String persistenceUnitName) {
    pmf = JDOHelper.getPersistenceManagerFactory(persistenceUnitName);
  }

  public PersistenceHelperImpl() {
    pmf = JDOHelper.getPersistenceManagerFactory(Constants.PORTFOLIO_PERSISTENCE_UNIT);
  }

  public PortfolioDO findPortfolio(long portfolioId) {
    PersistenceManager manager = getPersistenceManager();
    Transaction tx = manager.currentTransaction();
    try {
      tx.begin();
      manager.getFetchPlan().setMaxFetchDepth(3);
      Query<PortfolioDO> q = manager.<PortfolioDO>newQuery(PortfolioDO.class);
      q.filter("id == " + portfolioId);
      PortfolioDO portfolio = q.executeUnique();
      tx.commit();
      return portfolio;
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      manager.close();
    }
  }

  public void savePortfolio(PortfolioDO portfolio) {
    final PersistenceManager manager = getPersistenceManager();
    Transaction tx = manager.currentTransaction();
    try {
      tx.begin();
      manager.getFetchPlan().setMaxFetchDepth(3);
      manager.makePersistent(portfolio);
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      manager.close();
    }
  }

  public void removePortfolio(PortfolioDO portfolio) {
    final PersistenceManager manager = getPersistenceManager();
    Transaction tx = manager.currentTransaction();
    try {
      tx.begin();
      portfolio.setConstituents(Collections.emptySet());
      manager.deletePersistent(portfolio);
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      manager.close();
    }
  }

  @Override
  public Collection<InstrumentDO> getAllStockInstruments() {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<InstrumentDO> q = pm.<InstrumentDO>newQuery(InstrumentDO.class);
      q.filter("instrumentType == '" + InstrumentType.STOCK.toString() + "'");
      List<InstrumentDO> instruments = q.executeList();
      tx.commit();
      return instruments;
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      pm.close();
    }
  }

  private PersistenceManager getPersistenceManager() {
    return pmf.getPersistenceManager();
  }
}
