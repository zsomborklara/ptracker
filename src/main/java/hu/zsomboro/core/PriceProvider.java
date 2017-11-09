package hu.zsomboro.core;

import java.time.LocalDate;

public interface PriceProvider {

  Price getQuote(Instrument instrument, LocalDate asOfDate);

  boolean hasPriceFor(Instrument instrument);
}
