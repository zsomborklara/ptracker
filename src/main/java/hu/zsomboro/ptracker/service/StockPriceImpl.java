package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

import hu.zsomboro.core.security.HasPrice;
import hu.zsomboro.ptracker.core.Price;

public class StockPriceImpl implements PriceService {

  @Override
  public Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Price> getAllPricesForDay(LocalDate asOfDate) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void savePirce(LocalDate asOfDate, HasPrice pricedInstrument, Price price) {
    // TODO Auto-generated method stub

  }

}
