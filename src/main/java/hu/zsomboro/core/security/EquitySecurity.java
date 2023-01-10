package hu.zsomboro.core.security;

public class EquitySecurity extends Instrument {

  protected EquitySecurity(String name, String identifier, InstrumentType instrumentType) {
    super(name, identifier, instrumentType);
  }

  public static EquitySecurity newStock(String name, String identifier) {
    return new EquitySecurity(name, identifier, InstrumentType.STOCK);
  }

  public static EquitySecurity newETF(String name, String identifier) {
    return new EquitySecurity(name, identifier, InstrumentType.EXCHANGE_TRADED_FUND);
  }

}
