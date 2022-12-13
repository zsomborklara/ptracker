package hu.zsomboro.persistence;

import hu.zsomboro.core.Price;

import java.time.LocalDate;
import java.util.Map;

public interface StockPriceDao {

  public Map<String, Price> getPrice(LocalDate asOfDate);

}
