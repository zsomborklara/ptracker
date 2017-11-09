package hu.zsomboro.persistence;

import hu.zsomboro.core.Portfolio;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchPlan;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
@FetchPlan(maxFetchDepth = 3)
public class ConstituentDO {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
  private long id;
  @Persistent(defaultFetchGroup = "true")
  private InstrumentDO instrument;
  private int number;

  public ConstituentDO(InstrumentDO instrument, int number) {
    this.instrument = instrument;
    this.number = number;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public InstrumentDO getInstrument() {
    return instrument;
  }

  public void setInstrument(InstrumentDO instrument) {
    this.instrument = instrument;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void addCoreObject(Portfolio.Builder builder) {
    builder.add(instrument.toCoreObject(), number);
  }
}
