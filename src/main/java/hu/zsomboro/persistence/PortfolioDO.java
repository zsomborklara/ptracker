package hu.zsomboro.persistence;

import com.google.common.collect.Sets;
import hu.zsomboro.common.Constants;
import hu.zsomboro.core.Portfolio;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class PortfolioDO {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
  private long id;
  @Persistent(defaultFetchGroup = "true")
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
