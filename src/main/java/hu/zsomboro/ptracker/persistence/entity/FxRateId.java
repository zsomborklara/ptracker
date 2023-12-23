package hu.zsomboro.ptracker.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;

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
    return  EqualsBuilder.reflectionEquals(this, obj);
  }

}