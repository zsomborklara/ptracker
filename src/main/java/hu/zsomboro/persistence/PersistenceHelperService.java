package hu.zsomboro.persistence;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import hu.zsomboro.persistence.entity.InstrumentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

public interface PersistenceHelperService {

  public PortfolioDO findPortfolio(long portfolioId);

  @Transactional(value = TxType.REQUIRED)
  public void savePortfolio(PortfolioDO portfolio);

  @Transactional(value = TxType.REQUIRED)
  public void removePortfolio(PortfolioDO portfolio);

  public Collection<InstrumentDO> getAllStockInstruments();
}
