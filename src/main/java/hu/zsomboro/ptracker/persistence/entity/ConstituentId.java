package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.Embeddable;

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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConstituentId that = (ConstituentId) o;

    if (portfolioId != that.portfolioId) return false;
    return instrumentId.equals(that.instrumentId);
  }

  @Override
  public int hashCode() {
    int result = (int) (portfolioId ^ (portfolioId >>> 32));
    result = 31 * result + instrumentId.hashCode();
    return result;
  }
}