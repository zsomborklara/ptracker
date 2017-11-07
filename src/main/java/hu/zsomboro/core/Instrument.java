package hu.zsomboro.core;

import com.google.common.base.Preconditions;

public class Instrument {

  private final String name;
  private final String identifier;
  private final IdentifierType idType;

  public Instrument(String name, String identifier, IdentifierType idType) {

    Preconditions.checkNotNull(name, "Instrument name is missing");
    Preconditions.checkNotNull(identifier, "Instrument identifier is missing");
    Preconditions.checkNotNull(idType, "Instrument identifier type is missing");

    this.name = name;
    this.identifier = identifier;
    this.idType = idType;
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
}
