package hu.zsomboro.persistence;

import java.util.Collection;

public interface PersistenceHelperDAO {

  public PortfolioDO findPortfolio(long portfolioId);

  public void savePortfolio(PortfolioDO portfolio);

  public void removePortfolio(PortfolioDO portfolio);

  public Collection<InstrumentDO> getAllStockInstruments();
}
