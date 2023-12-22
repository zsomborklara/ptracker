package hu.zsomboro.ptracker.persistence.entity;

import java.util.Set;

import com.google.common.collect.Sets;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class PortfolioDO {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "portfoliodo_seq", initialValue = 1, allocationSize = 10)
  private long id;
  @OneToMany(targetEntity = ConstituentDO.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ConstituentDO> constituents;
  @OneToOne(targetEntity = CashDO.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private CashDO cash;
  @Column(unique = true)
  private String name;

  public PortfolioDO(String name) {
    this.name = name;
  }

  public PortfolioDO() {
    super();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<ConstituentDO> getConstituents() {
    return constituents;
  }

  public void setConstituents(Set<ConstituentDO> constituents) {
    this.constituents = constituents;
  }

  public CashDO getCash() {
    return cash;
  }

  public void setCash(CashDO cash) {
    this.cash = cash;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
