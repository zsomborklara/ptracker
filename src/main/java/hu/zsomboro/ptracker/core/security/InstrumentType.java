package hu.zsomboro.ptracker.core.security;

import java.time.LocalDate;

public enum InstrumentType {

  STOCK, TREASURY_BOND, DEPOSIT, PENSION_FUND, EXCHANGE_TRADED_FUND;

  public Instrument create(String name, String identifier) {

      return switch (this) {
          case STOCK, EXCHANGE_TRADED_FUND -> new EquitySecurity(name, identifier, this);
          default -> throw new IllegalStateException("Unexpected instrument type " + this);
      };
  }

  public Instrument createWithInterest(String name, String identifier, LocalDate maturity, double interestRate) {
      return switch (this) {
          case DEPOSIT, PENSION_FUND, TREASURY_BOND ->
                  new FixedIncomeSecurity(name, identifier, this, maturity, interestRate);
          default -> throw new IllegalStateException("Unexpected instrument type " + this);
      };
  }

}
