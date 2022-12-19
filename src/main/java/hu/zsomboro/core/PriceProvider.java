package hu.zsomboro.core;

import java.time.LocalDate;

import hu.zsomboro.core.security.Instrument;

public interface PriceProvider {

  Price getQuote(Instrument instrument, LocalDate asOfDate);

  boolean hasPriceFor(Instrument instrument);
}
