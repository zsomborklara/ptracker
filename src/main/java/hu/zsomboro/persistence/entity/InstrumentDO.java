package hu.zsomboro.persistence.entity;

import hu.zsomboro.core.IdentifierType;
import hu.zsomboro.core.Instrument;
import hu.zsomboro.core.InstrumentType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InstrumentDO {

  @Id
  private String identifier;
  private String idType;
  private String instrumentType;
  private String name;

  public InstrumentDO(String name, String identifier, String idType, String instrumentType) {
    this.name = name;
    this.identifier = identifier;
    this.idType = idType;
    this.instrumentType = instrumentType;
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

  public String getIdType() {
    return idType;
  }

  public void setIdType(String idType) {
    this.idType = idType;
  }

  public String getInstrumentType() {
    return instrumentType;
  }

  public void setInstrumentType(String instrumentType) {
    this.instrumentType = instrumentType;
  }

  public Instrument toCoreObject() {
    return new Instrument(this.name, this.identifier,
        IdentifierType.valueOf(this.idType), InstrumentType.valueOf(this.instrumentType));
  }
}
