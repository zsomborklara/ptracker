package hu.zsomboro.persistence;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import hu.zsomboro.core.security.InstrumentType;
import hu.zsomboro.persistence.entity.InstrumentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;
import hu.zsomboro.persistence.repository.InstrumentDORepository;
import hu.zsomboro.persistence.repository.PortfolioDORepository;

@Service
public class PersistenceHelperImpl implements PersistenceHelperService {

  private static final Logger LOG = LogManager.getLogger(PersistenceHelperImpl.class);

  private final PortfolioDORepository portfolioRepository;
  private final InstrumentDORepository instrumentRepositry;

  public PersistenceHelperImpl(PortfolioDORepository portfolioRepository, InstrumentDORepository instrumentRepositry) {
    super();
    this.portfolioRepository = portfolioRepository;
    this.instrumentRepositry = instrumentRepositry;
  }

  @Override
  public PortfolioDO findPortfolio(long portfolioId) {
    return portfolioRepository.findById(portfolioId);
  }

  @Override
  public void savePortfolio(PortfolioDO portfolio) {
    portfolioRepository.save(portfolio);
  }

  @Override
  public void removePortfolio(PortfolioDO portfolio) {
    portfolioRepository.delete(portfolio);
  }

  @Override
  public Collection<InstrumentDO> getAllStockInstruments() {
    return instrumentRepositry.findByInstrumentType(InstrumentType.STOCK.toString());
  }

}
