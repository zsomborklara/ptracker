package hu.zsomboro.persistence;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import hu.zsomboro.common.CoreToPersistenceMapper;
import hu.zsomboro.core.Portfolio;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.core.security.InstrumentType;
import hu.zsomboro.persistence.entity.PortfolioDO;
import hu.zsomboro.persistence.repository.InstrumentDORepository;
import hu.zsomboro.persistence.repository.PortfolioDORepository;

@Service
public class PersistenceHelperImpl implements PersistenceHelperService {

  private static final Logger LOG = LogManager.getLogger(PersistenceHelperImpl.class);

  private final PortfolioDORepository portfolioRepository;
  private final InstrumentDORepository instrumentRepositry;
  private final CoreToPersistenceMapper mapper;

  public PersistenceHelperImpl(PortfolioDORepository portfolioRepository, InstrumentDORepository instrumentRepositry,
      CoreToPersistenceMapper mapper) {
    super();
    this.portfolioRepository = portfolioRepository;
    this.instrumentRepositry = instrumentRepositry;
    this.mapper = mapper;
  }

  @Override
  public Portfolio findPortfolio(String name) {
    List<PortfolioDO> foundPortfolios = portfolioRepository.findByName(name);
    if (foundPortfolios.size() > 1) {
      throw new IllegalStateException("Multiple porfolios found with name " + name);
    }

    if (foundPortfolios.isEmpty()) {
      return Portfolio.EMPTY;
    }
    return this.mapper.map(foundPortfolios.get(0), Portfolio.class);
  }

  @Override
  @Transactional(value = TxType.REQUIRED)
  public void savePortfolio(Portfolio portfolio) {
    portfolioRepository.save(mapper.map(portfolio, PortfolioDO.class));
  }

  @Override
  @Transactional(value = TxType.REQUIRED)
  public void removePortfolio(Portfolio portfolio) {
    portfolioRepository.delete(mapper.map(portfolio, PortfolioDO.class));
  }

  @Override
  public Collection<Instrument> getAllStockInstruments() {
    return instrumentRepositry.findByInstrumentType(InstrumentType.STOCK.toString()).stream()
        .map(instrument -> mapper.map(instrument, Instrument.class)).collect(Collectors.toList());
  }

  @Override
  public void newPortfolio(String name) {
    Portfolio newPortfolio = new Portfolio.Builder().withName(name).build();
    portfolioRepository.save(mapper.map(newPortfolio, PortfolioDO.class));
  }

}
