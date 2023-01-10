package hu.zsomboro.core.security;

import java.time.LocalDate;

public class FixedIncomeSecurity extends Instrument implements HasMaturity {

  private final LocalDate maturity;
  private final double interestRate;

  protected FixedIncomeSecurity(String name, String identifier, InstrumentType instrumentType, LocalDate maturity,
      double interestRate) {
    super(name, identifier, instrumentType);
    this.maturity = maturity;
    this.interestRate = interestRate;
  }

  @Override
  public LocalDate getMaturity() {
    return this.maturity;
  }

  @Override
  public double getInterestRate() {
    return this.interestRate;
  }

  public static FixedIncomeSecurity newDeposit(String name, String identifier, LocalDate maturity,
      double interestRate) {
    return new FixedIncomeSecurity(name, identifier, InstrumentType.DEPOSIT, maturity, interestRate);
  }

  public static FixedIncomeSecurity newPensionFund(String name, String identifier, LocalDate maturity,
      double interestRate) {
    return new FixedIncomeSecurity(name, identifier, InstrumentType.PENSION_FUND, maturity, interestRate);
  }

  public static FixedIncomeSecurity newTBond(String name, String identifier, LocalDate maturity, double interestRate) {
    return new FixedIncomeSecurity(name, identifier, InstrumentType.TREASURY_BOND, maturity, interestRate);
  }

}
