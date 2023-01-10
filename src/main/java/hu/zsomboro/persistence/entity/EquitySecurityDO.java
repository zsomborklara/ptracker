package hu.zsomboro.persistence.entity;

import javax.persistence.Entity;

import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.InstrumentType;

@Entity
public class EquitySecurityDO extends InstrumentDO {

  public EquitySecurityDO(String name, String identifier, String instrumentType) {
    super(name, identifier, instrumentType);
  }

  public EquitySecurityDO() {
    super();
  }

  public EquitySecurity toCoreObject() {
    return (EquitySecurity) InstrumentType.valueOf(getInstrumentType()).create(getName(), getIdentifier(), null, 0.0);
  }
}
