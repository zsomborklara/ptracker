package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;

public interface LoadedPriceService {

  public Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument);

  public Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument);

  public Map<String, Price> getAllPricesForDay(LocalDate asOfDate);

}
