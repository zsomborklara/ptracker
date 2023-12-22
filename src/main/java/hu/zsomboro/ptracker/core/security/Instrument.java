package hu.zsomboro.ptracker.core.security;

import com.google.common.base.Preconditions;

public abstract class Instrument {

  private final String name;
  private final String instrumentId;
  private final InstrumentType instrumentType;

  public Instrument(String name, String instrumentId, InstrumentType instrumentType) {

    Preconditions.checkNotNull(name, "Instrument name is missing");
    Preconditions.checkNotNull(instrumentId, "Instrument identifier is missing");
    Preconditions.checkNotNull(instrumentType, "Instrument type is missing");

    this.name = name;
    this.instrumentId = instrumentId;
    this.instrumentType = instrumentType;
  }

  public String getName() {
    return name;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public InstrumentType getInstrumentType() {
    return instrumentType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Instrument that = (Instrument) o;

    if (!instrumentId.equals(that.instrumentId))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return instrumentId.hashCode();
  }

}
