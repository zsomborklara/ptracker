package hu.zsomboro.ptracker.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(FxRateId.class)
public class HufFxRateDO {

  @Id
  private String isoCurrency;
  @Id
  private LocalDate asOfDate;

  private double value;

  public HufFxRateDO() {
    super();
  }

  public LocalDate getAsOfDate() {
    return asOfDate;
  }

  public void setAsOfDate(LocalDate asOfDate) {
    this.asOfDate = asOfDate;
  }

  public String getIsoCurrency() {
    return isoCurrency;
  }

  public void setIsoCurrency(String isoCurrency) {
    this.isoCurrency = isoCurrency;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(asOfDate, isoCurrency);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HufFxRateDO other = (HufFxRateDO) obj;
    return Objects.equals(asOfDate, other.asOfDate) && Objects.equals(isoCurrency, other.isoCurrency);
  }

}
