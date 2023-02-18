package hu.zsomboro.ptracker.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Streams;

import hu.zsomboro.ptracker.common.CoreToPersistenceMapper;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.core.security.Instrument;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.InstrumentDORepository;
import hu.zsomboro.ptracker.persistence.PortfolioDORepository;
import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;

@Service
public class PortfolioServiceImpl implements PortfolioService {

  private static final Logger LOG = LogManager.getLogger(PortfolioServiceImpl.class);

  private final PortfolioDORepository portfolioRepository;
  private final InstrumentDORepository instrumentRepositry;
  private final CoreToPersistenceMapper mapper;

  public PortfolioServiceImpl(PortfolioDORepository portfolioRepository, InstrumentDORepository instrumentRepositry,
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
  @Transactional(propagation = Propagation.REQUIRED)
  public void savePortfolio(Portfolio portfolio) {
    portfolioRepository.save(mapper.map(portfolio, PortfolioDO.class));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void removePortfolio(Portfolio portfolio) {
    portfolioRepository.delete(mapper.map(portfolio, PortfolioDO.class));
  }

  @Override
  public Collection<HasPrice> getAllPriceableInstruments() {
    List<InstrumentDO> stocks = instrumentRepositry.findByInstrumentType(InstrumentType.STOCK.toString());
    List<InstrumentDO> etfs = instrumentRepositry.findByInstrumentType(InstrumentType.EXCHANGE_TRADED_FUND.toString());
    return Streams.concat(stocks.stream(), etfs.stream()).map(instrument -> mapper.map(instrument, Instrument.class))
        .map(HasPrice.class::cast).collect(Collectors.toList());
  }

  @Override
  public void newPortfolio(String name) {
    Portfolio newPortfolio = new Portfolio.Builder().withName(name).build();
    portfolioRepository.save(mapper.map(newPortfolio, PortfolioDO.class));
  }

}
