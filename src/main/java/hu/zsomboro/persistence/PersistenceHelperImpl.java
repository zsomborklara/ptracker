package hu.zsomboro.persistence;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import hu.zsomboro.core.Portfolio;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.core.security.InstrumentType;
import hu.zsomboro.persistence.entity.InstrumentDO;
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
  public Portfolio findPortfolio(String name) {
    return portfolioRepository.findByName(name).get(0).toCoreObject();
  }

  @Override
  @Transactional(value = TxType.REQUIRED)
  public void savePortfolio(Portfolio portfolio) {
    portfolioRepository.save(portfolio.toDataObject());
  }

  @Override
  @Transactional(value = TxType.REQUIRED)
  public void removePortfolio(Portfolio portfolio) {
    portfolioRepository.delete(portfolio.toDataObject());
  }

  @Override
  public Collection<Instrument> getAllStockInstruments() {
    return instrumentRepositry.findByInstrumentType(InstrumentType.STOCK.toString()).stream()
        .map(InstrumentDO::toCoreObject).collect(Collectors.toList());
  }

}
