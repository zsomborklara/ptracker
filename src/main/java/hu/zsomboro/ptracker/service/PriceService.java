package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;

public interface PriceService {

  public Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument);

  public void savePirce(LocalDate asOfDate, HasPrice pricedInstrument, Price price);

  public Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument);

  public Map<String, Price> getAllPricesForDay(LocalDate asOfDate);

}
