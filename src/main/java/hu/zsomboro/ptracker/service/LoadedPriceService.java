package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;

public interface LoadedPriceService {

  Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument);

  Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument);

  Map<String, Price> getAllPricesForDay(LocalDate asOfDate);

}
