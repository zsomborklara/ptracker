package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.Entity;

@Entity
public class EquitySecurityDO extends InstrumentDO {

  public EquitySecurityDO(String name, String identifier, String instrumentType) {
    super(name, identifier, instrumentType);
  }

  public EquitySecurityDO() {
    super();
  }

}
