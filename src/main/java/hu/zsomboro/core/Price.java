package hu.zsomboro.core;

import java.time.LocalDate;
import java.util.Currency;

public class Price {

  private final double value;
  private final LocalDate asOfDate;
  private final Currency priceCurrency;

  public Price(double value, LocalDate asOfDate, Currency priceCurrency) {
    this.value = value;
    this.asOfDate = asOfDate;
    this.priceCurrency = priceCurrency;
  }

  public double getValue() {
    return value;
  }

  public LocalDate getAsOfDate() {
    return asOfDate;
  }

  public Currency getPriceCurrency() {
    return priceCurrency;
  }
}
