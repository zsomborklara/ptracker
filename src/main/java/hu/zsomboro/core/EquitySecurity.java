package hu.zsomboro.core;

import hu.zsomboro.persistence.entity.EquitySecurityDO;

public class EquitySecurity extends Instrument {

  public EquitySecurity(String name, String identifier, InstrumentType instrumentType) {
    super(name, identifier, instrumentType);
  }

  @Override
  public EquitySecurityDO toDataObject() {
    return new EquitySecurityDO(getName(), getIdentifier(), getInstrumentType().toString());
  }

}
