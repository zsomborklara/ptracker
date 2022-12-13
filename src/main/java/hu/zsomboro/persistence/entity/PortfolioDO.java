package hu.zsomboro.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;

import hu.zsomboro.core.Portfolio;

@Entity
public class PortfolioDO {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(targetEntity = ConstituentDO.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ConstituentDO> constituents;

  public PortfolioDO(Set<ConstituentDO> constituents) {
    this.constituents = Sets.newHashSet(constituents);
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

  public Portfolio toCoreObject() {
    Portfolio.Builder builder = new Portfolio.Builder();
    for (ConstituentDO cdo : constituents) {
      cdo.addCoreObject(builder);
    }
    return builder.build();
  }
}
