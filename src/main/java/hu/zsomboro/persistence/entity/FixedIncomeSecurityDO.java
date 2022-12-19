package hu.zsomboro.persistence.entity;

import java.time.LocalDate;

import javax.persistence.Entity;

import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.InstrumentType;

@Entity
public class FixedIncomeSecurityDO extends InstrumentDO {

  private double interestRate;

  private LocalDate maturity;

  public FixedIncomeSecurityDO(String name, String identifier, String instrumentType, LocalDate maturity,
      double interestRate) {
    super(name, identifier, instrumentType);
    this.maturity = maturity;
    this.interestRate = interestRate;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }

  public LocalDate getMaturity() {
    return maturity;
  }

  public void setMaturity(LocalDate maturity) {
    this.maturity = maturity;
  }

  @Override
  public FixedIncomeSecurity toCoreObject() {
    return (FixedIncomeSecurity) InstrumentType.valueOf(getInstrumentType()).create(getName(), getIdentifier(),
        getMaturity(), getInterestRate());
  }
}
