package hu.zsomboro.ptracker.service;

import java.util.Collection;

import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.HasPrice;

public interface PortfolioService {

  Portfolio findPortfolio(String name);

  void savePortfolio(Portfolio portfolio);

  void newPortfolio(String name);

  void removePortfolio(Portfolio portfolio);

  Collection<HasPrice> getAllPriceableInstruments();

}
