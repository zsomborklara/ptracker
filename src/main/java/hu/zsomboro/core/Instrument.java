package hu.zsomboro.core;

import com.google.common.base.Preconditions;

import hu.zsomboro.persistence.entity.InstrumentDO;

public class Instrument {

  private final String name;
  private final String identifier;
  private final IdentifierType idType;
  private final InstrumentType instrumentType;

  public Instrument(String name, String identifier, IdentifierType idType, InstrumentType instrumentType) {

    Preconditions.checkNotNull(name, "Instrument name is missing");
    Preconditions.checkNotNull(identifier, "Instrument identifier is missing");
    Preconditions.checkNotNull(idType, "Instrument identifier type is missing");
    Preconditions.checkNotNull(instrumentType, "Instrument type is missing");

    this.name = name;
    this.identifier = identifier;
    this.idType = idType;
    this.instrumentType = instrumentType;
  }

  public String getName() {
    return name;
  }

  public String getIdentifier() {
    return identifier;
  }

  public IdentifierType getIdType() {
    return idType;
  }

  public InstrumentType getInstrumentType() {
    return instrumentType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Instrument that = (Instrument) o;

    if (!identifier.equals(that.identifier)) return false;
    if (idType != that.idType) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = identifier.hashCode();
    result = 31 * result + idType.hashCode();
    return result;
  }

  public InstrumentDO toDataObject() {
    return new InstrumentDO(this.name, this.identifier,
        this.idType.toString(), this.instrumentType.toString());
  }
}
