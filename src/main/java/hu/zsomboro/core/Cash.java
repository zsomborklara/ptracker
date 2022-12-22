package hu.zsomboro.core;

import java.util.Objects;

import hu.zsomboro.persistence.entity.CashDO;

public class Cash {

  public static final Cash ZERO = new Cash(0, "HUF");

  private final double amount;
  private final String currency;

  public Cash(double amount, String currency) {
    super();
    this.amount = amount;
    this.currency = currency;
  }

  public CashDO toDataObject() {
    return new CashDO(currency, amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cash other = (Cash) obj;
    return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
        && Objects.equals(currency, other.currency);
  }

}
