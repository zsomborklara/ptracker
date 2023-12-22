package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class InstrumentDO {

  @Id
  private String instrumentId;
  private String instrumentType;
  private String name;

  public InstrumentDO(String name, String instrumentId, String instrumentType) {
    this.name = name;
    this.instrumentId = instrumentId;
    this.instrumentType = instrumentType;
  }

  public InstrumentDO() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public String getInstrumentType() {
    return instrumentType;
  }

  public void setInstrumentType(String instrumentType) {
    this.instrumentType = instrumentType;
  }

}
