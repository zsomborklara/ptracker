package hu.zsomboro.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import hu.zsomboro.persistence.StockPriceDao;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class StockPriceProvider implements PriceProvider {

  private final LoadingCache<LocalDate, Map<String, Price>> priceCache;
  private final StockPriceDao stockPriceDao;

  public StockPriceProvider(StockPriceDao stockPriceDao) {
    CacheLoader<LocalDate, Map<String, Price>> loader;
    loader = new CacheLoader<LocalDate, Map<String, Price>>() {
      @Override
      public Map<String, Price> load(LocalDate key) {
        return Maps.newHashMap();
      }
    };
    this.priceCache = CacheBuilder.newBuilder().build(loader);
    this.stockPriceDao = stockPriceDao;
  }

  @Override
  public Price getQuote(Instrument instrument, LocalDate asOfDate) {
    try {
      return priceCache.get(asOfDate).get(instrument.getIdentifier());
    } catch (ExecutionException e) {
      throw new IllegalStateException("Could not load prices for "
          + instrument + " at " + asOfDate, e);
    }
  }

  @Override
  public boolean hasPriceFor(Instrument instrument) {
    return instrument.getInstrumentType() == InstrumentType.STOCK;
  }
}
