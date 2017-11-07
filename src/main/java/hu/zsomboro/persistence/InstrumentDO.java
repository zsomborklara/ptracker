package hu.zsomboro.persistence;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class InstrumentDO {

  @Persistent(defaultFetchGroup = "true")
  private String name;
  @PrimaryKey
  @Persistent
  private String identifier;
  @Persistent(defaultFetchGroup = "true")
  private String idType;

  public InstrumentDO(String name, String identifier, String idType) {
    this.name = name;
    this.identifier = identifier;
    this.idType = idType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getIdType() {
    return idType;
  }

  public void setIdType(String idType) {
    this.idType = idType;
  }
}
