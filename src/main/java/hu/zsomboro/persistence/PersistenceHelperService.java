package hu.zsomboro.persistence;

import java.util.Collection;

import hu.zsomboro.core.Portfolio;
import hu.zsomboro.core.security.Instrument;

public interface PersistenceHelperService {

  public Portfolio findPortfolio(String name);

  public void savePortfolio(Portfolio portfolio);

  public void removePortfolio(Portfolio portfolio);

  public Collection<Instrument> getAllStockInstruments();
}
