package hu.zsomboro.ptracker.persistence.entity;

import java.time.LocalDate;

import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import jakarta.persistence.Entity;

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

  public FixedIncomeSecurityDO() {
    super();
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

  public FixedIncomeSecurity toCoreObject() {
    return (FixedIncomeSecurity) InstrumentType.valueOf(getInstrumentType()).createWithInterest(getName(), getInstrumentId(),
        getMaturity(), getInterestRate());
  }
}
