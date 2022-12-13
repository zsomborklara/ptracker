package hu.zsomboro.persistence;

import java.time.LocalDate;
import java.util.Map;

import hu.zsomboro.core.Price;

public class StockPriceImpl implements StockPriceDao {

  @Override
  public Map<String, Price> getPrice(LocalDate asOfDate) {
    return null;
  }
}
