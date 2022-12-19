package hu.zsomboro.core.security;

import hu.zsomboro.persistence.entity.EquitySecurityDO;

public final class EquitySecurity extends Instrument {

  protected EquitySecurity(String name, String identifier, InstrumentType instrumentType) {
    super(name, identifier, instrumentType);
  }

  @Override
  public EquitySecurityDO toDataObject() {
    return new EquitySecurityDO(getName(), getIdentifier(), getInstrumentType().toString());
  }

  public static EquitySecurity newStock(String name, String identifier) {
    return new EquitySecurity(name, identifier, InstrumentType.STOCK);
  }

  public static EquitySecurity newETF(String name, String identifier) {
    return new EquitySecurity(name, identifier, InstrumentType.EXCHANGE_TRADED_FUND);
  }

}
