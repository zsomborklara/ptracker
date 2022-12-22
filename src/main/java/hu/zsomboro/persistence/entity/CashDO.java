package hu.zsomboro.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hu.zsomboro.core.Cash;
import hu.zsomboro.core.Portfolio;

@Entity
public class CashDO {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String currency;
  private double amount;

  public CashDO(String currency, double amount) {
    super();
    this.currency = currency;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void addCoreObject(Portfolio.Builder builder) {
    builder.add(new Cash(amount, currency));
  }
}
