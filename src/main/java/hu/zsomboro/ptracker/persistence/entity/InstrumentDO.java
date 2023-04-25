package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class InstrumentDO {

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

}
