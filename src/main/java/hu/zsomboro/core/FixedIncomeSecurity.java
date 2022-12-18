package hu.zsomboro.core;

import java.time.LocalDate;

import hu.zsomboro.persistence.entity.FixedIncomeSecurityDO;

public class FixedIncomeSecurity extends Instrument implements HasMaturity {

  private final LocalDate maturity;
  private final double interestRate;

  public FixedIncomeSecurity(String name, String identifier, InstrumentType instrumentType, LocalDate maturity,
      double interestRate) {
    super(name, identifier, instrumentType);
    this.maturity = maturity;
    this.interestRate = interestRate;
  }

  @Override
  public LocalDate getMaturityDate() {
    return this.maturity;
  }

  @Override
  public double getInterestRate() {
    return this.interestRate;
  }

  @Override
  public FixedIncomeSecurityDO toDataObject() {
    return new FixedIncomeSecurityDO(getName(), getIdentifier(), getIdentifier(), maturity, interestRate);
  }

}
