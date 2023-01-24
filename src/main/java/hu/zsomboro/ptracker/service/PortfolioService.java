package hu.zsomboro.ptracker.service;

import java.util.Collection;

import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.ptracker.core.Portfolio;

public interface PortfolioService {

  public Portfolio findPortfolio(String name);

  public void savePortfolio(Portfolio portfolio);

  public void newPortfolio(String name);

  public void removePortfolio(Portfolio portfolio);

  public Collection<Instrument> getAllStockInstruments();

}
