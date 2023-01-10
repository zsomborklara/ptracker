package hu.zsomboro.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import hu.zsomboro.core.security.Instrument;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class InstrumentDO {

  @Id
  private String identifier;
  private String instrumentType;
  private String name;

  public InstrumentDO(String name, String identifier, String instrumentType) {
    this.name = name;
    this.identifier = identifier;
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

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getInstrumentType() {
    return instrumentType;
  }

  public void setInstrumentType(String instrumentType) {
    this.instrumentType = instrumentType;
  }

  public abstract Instrument toCoreObject();

}
