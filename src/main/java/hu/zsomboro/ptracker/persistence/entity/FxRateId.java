package hu.zsomboro.ptracker.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class FxRateId implements Serializable {

  private static final long serialVersionUID = 7849051526180160317L;
  private String isoCurrency;
  private LocalDate asOfDate;

  public FxRateId() {
    super();
  }

  public String getIsoCurrency() {
    return isoCurrency;
  }

  public void setIsoCurrency(String isoCurrency) {
    this.isoCurrency = isoCurrency;
  }

  public LocalDate getAsOfDate() {
    return asOfDate;
  }

  public void setAsOfDate(LocalDate asOfDate) {
    this.asOfDate = asOfDate;
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
    FxRateId other = (FxRateId) obj;
    return Objects.equals(asOfDate, other.asOfDate) && Objects.equals(isoCurrency, other.isoCurrency);
  }

}