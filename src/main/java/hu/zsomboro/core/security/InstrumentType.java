package hu.zsomboro.core.security;

import java.time.LocalDate;

import com.google.common.base.Preconditions;

public enum InstrumentType {

  STOCK, TREASURY_BOND, CASH, DEPOSIT, PENSION_FUND, EXCHANGE_TRADED_FUND;

  public Instrument create(String name, String identifier, LocalDate maturity, double interestRate) {

    switch (this) {
    case STOCK:
    case EXCHANGE_TRADED_FUND: {
      return new EquitySecurity(name, identifier, this);
    }
    case DEPOSIT:
    case PENSION_FUND:
    case TREASURY_BOND: {
      Preconditions.checkNotNull(maturity, "Cannot have a fixed income security with a missing maturity");
      Preconditions.checkArgument(interestRate > 0., "Cannot have a negative interest rate");
      return new FixedIncomeSecurity(name, identifier, this, maturity, interestRate);
    }
    case CASH:
      return null;
    default:
      throw new IllegalStateException("Unexpected instrument type " + this);
    }
  }

}
