package hu.zsomboro.persistence;

import hu.zsomboro.common.Constants;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class PersistenceHelperImpl implements PersistenceHelperDAO {

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

  public PortfolioDO findPortfolio(int portfolioId) {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PortfolioDO> q = pm.<PortfolioDO>newQuery("SELECT FROM " + PortfolioDO.class.getName() +
          " WHERE id == " + portfolioId);
      PortfolioDO portfolio = q.executeUnique();
      tx.commit();
      return portfolio;
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      pm.close();
    }
  }

  public void savePortfolio(PortfolioDO portfolio) {
    final PersistenceManager manager = getPersistenceManager();
    Transaction tx = manager.currentTransaction();
    try {
      tx.begin();
      manager.makePersistent(portfolio);
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      manager.close();
    }
  }

  private PersistenceManager getPersistenceManager() {
    return pmf.getPersistenceManager();
  }
}
