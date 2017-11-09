package hu.zsomboro.persistence;

import com.google.common.collect.Sets;
import hu.zsomboro.core.Instrument;
import hu.zsomboro.core.Price;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class StockPriceImpl implements StockPriceDao {

  @Override
  public Map<String, Price> getPrice(LocalDate asOfDate) {
    return null;
  }
}
