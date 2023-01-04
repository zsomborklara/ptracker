package hu.zsomboro.core;

import hu.zsomboro.persistence.entity.CashDO;

public record Cash(double amount, String currency) {

  public static final Cash ZERO = new Cash(0, "HUF");

  public CashDO toDataObject() {
    return new CashDO(currency, amount);
  }

}
