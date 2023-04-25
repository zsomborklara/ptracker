package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ConstituentDO {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @OneToOne(targetEntity = InstrumentDO.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private InstrumentDO instrument;
  private int number;

  public ConstituentDO(InstrumentDO instrument, int number) {
    this.instrument = instrument;
    this.number = number;
  }

  public ConstituentDO() {
    super();
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
