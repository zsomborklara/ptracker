package hu.zsomboro.ptracker.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PriceId implements Serializable {

  private static final long serialVersionUID = 907608141255463210L;
  private String identifier;
  private LocalDate asOfDate;

  public PriceId() {
    super();
  }

  public PriceId(String identifier, LocalDate asOfDate) {
    super();
    this.identifier = identifier;
    this.asOfDate = asOfDate;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public LocalDate getAsOfDate() {
    return asOfDate;
  }

  public void setAsOfDate(LocalDate asOfDate) {
    this.asOfDate = asOfDate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(asOfDate, identifier);
  }

  @Override
  public boolean equals(Object obj) {
    return  EqualsBuilder.reflectionEquals(this, obj);
  }

}