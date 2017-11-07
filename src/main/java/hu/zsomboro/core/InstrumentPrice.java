package hu.zsomboro.core;

import java.time.LocalDate;

public class InstrumentPrice {

  private final LocalDate asOfDate;
  private final Instrument instrument;
  private final double price;

  public InstrumentPrice(LocalDate asOfDate, Instrument instrument, double price) {
    this.asOfDate = asOfDate;
    this.instrument = instrument;
    this.price = price;
  }

  public LocalDate getAsOfDate() {
    return asOfDate;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  public double getPrice() {
    return price;
  }
}
