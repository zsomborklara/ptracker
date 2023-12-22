package hu.zsomboro.ptracker.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class ConstituentDO {

  @EmbeddedId
  private ConstituentId id;

  @MapsId("instrumentId")
  @OneToOne(targetEntity = InstrumentDO.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private InstrumentDO instrument;

  @MapsId("portfolioId")
  @ManyToOne(targetEntity = PortfolioDO.class, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
  private PortfolioDO portfolioDO;
  private int number;

  public ConstituentDO(InstrumentDO instrument, int number, PortfolioDO portfolioDO) {
    this.instrument = instrument;
    this.number = number;
    this.portfolioDO = portfolioDO;
    this.id = new ConstituentId(portfolioDO.getId(), instrument.getInstrumentId());
  }

  public ConstituentDO() {
    super();
  }

  public InstrumentDO getInstrument() {
    return instrument;
  }

  public void setInstrument(InstrumentDO instrument) {
    this.instrument = instrument;
  }

  public PortfolioDO getPortfolioDO() {
    return portfolioDO;
  }

  public void setPortfolioDO(PortfolioDO portfolioDO) {
    this.portfolioDO = portfolioDO;
  }

  public ConstituentId getId() {
    return id;
  }

  public void setId(ConstituentId id) {
    this.id = id;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

}
