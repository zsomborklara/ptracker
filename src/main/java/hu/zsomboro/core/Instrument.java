package hu.zsomboro.core;

import com.google.common.base.Preconditions;

import hu.zsomboro.persistence.entity.InstrumentDO;

public abstract class Instrument {

  private final String name;
  private final String identifier;
  private final InstrumentType instrumentType;

  public Instrument(String name, String identifier, InstrumentType instrumentType) {

    Preconditions.checkNotNull(name, "Instrument name is missing");
    Preconditions.checkNotNull(identifier, "Instrument identifier is missing");
    Preconditions.checkNotNull(instrumentType, "Instrument type is missing");

    this.name = name;
    this.identifier = identifier;
    this.instrumentType = instrumentType;
  }

  public String getName() {
    return name;
  }

  public String getIdentifier() {
    return identifier;
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

    if (!identifier.equals(that.identifier))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return identifier.hashCode();
  }

  public abstract InstrumentDO toDataObject();
}
