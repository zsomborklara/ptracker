package hu.zsomboro.core;

public enum IdentifierType {
  ISIN(1),
  TICKER(2);

  private final int id;

  IdentifierType(int id) {
    this.id = id;
  }
}
