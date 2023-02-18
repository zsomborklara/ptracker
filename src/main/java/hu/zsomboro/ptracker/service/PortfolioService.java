package hu.zsomboro.ptracker.service;

import java.util.Collection;

import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.HasPrice;

public interface PortfolioService {

  public Portfolio findPortfolio(String name);

  public void savePortfolio(Portfolio portfolio);

  public void newPortfolio(String name);

  public void removePortfolio(Portfolio portfolio);

  public Collection<HasPrice> getAllPriceableInstruments();

}
