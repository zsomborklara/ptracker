package hu.zsomboro.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ConstituentDO {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
  private long id;
  @Persistent(defaultFetchGroup = "true")
  private InstrumentDO instrument;
  @Persistent(defaultFetchGroup = "true")
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
}
