package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConstituentId implements Serializable {

  public ConstituentId(long portfolioId, String instrumentId) {
    this.portfolioId = portfolioId;
    this.instrumentId = instrumentId;
  }

  public ConstituentId() {
  }

  private long portfolioId;
  private String instrumentId;

  public long getPortfolioId() {
    return portfolioId;
  }

  public void setPortfolioId(long portfolioId) {
    this.portfolioId = portfolioId;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    int result = (int) (portfolioId ^ (portfolioId >>> 32));
    result = 31 * result + instrumentId.hashCode();
    return result;
  }
}