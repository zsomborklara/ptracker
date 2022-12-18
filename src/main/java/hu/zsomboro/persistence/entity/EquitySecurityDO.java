package hu.zsomboro.persistence.entity;

import javax.persistence.Entity;

import hu.zsomboro.core.EquitySecurity;
import hu.zsomboro.core.InstrumentType;

@Entity
public class EquitySecurityDO extends InstrumentDO {

  public EquitySecurityDO(String name, String identifier, String instrumentType) {
    super(name, identifier, instrumentType);
  }

  public EquitySecurity toCoreObject() {
    return new EquitySecurity(getName(), getIdentifier(), InstrumentType.valueOf(getInstrumentType()));
  }
}
