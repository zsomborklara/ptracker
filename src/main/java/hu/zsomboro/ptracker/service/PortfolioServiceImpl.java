package hu.zsomboro.ptracker.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import hu.zsomboro.ptracker.common.PortfolioMapper;
import hu.zsomboro.ptracker.persistence.ConstituentDORepository;
import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Streams;

import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.InstrumentDORepository;
import hu.zsomboro.ptracker.persistence.PortfolioDORepository;
import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;

@Service
public class PortfolioServiceImpl implements PortfolioService {

  private static final Logger LOG = LogManager.getLogger(PortfolioServiceImpl.class);

  private final PortfolioDORepository portfolioRepository;
  private final ConstituentDORepository constituentDORepository;
  private final InstrumentDORepository instrumentRepositry;

  public PortfolioServiceImpl(PortfolioDORepository portfolioRepository, ConstituentDORepository constituentDORepository,
                              InstrumentDORepository instrumentRepositry) {
    super();
    this.portfolioRepository = portfolioRepository;
    this.constituentDORepository = constituentDORepository;
    this.instrumentRepositry = instrumentRepositry;
  }

  @Override
  public Portfolio findPortfolio(String name) {
    List<PortfolioDO> foundPortfolios = portfolioRepository.findByName(name);
    if (foundPortfolios.size() > 1) {
      throw new IllegalStateException("Multiple portfolios found with name " + name);
    }

    if (foundPortfolios.isEmpty()) {
      return Portfolio.EMPTY;
    }
    return PortfolioMapper.INSTANCE.portfolioDOToPortfolio(foundPortfolios.get(0));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void savePortfolio(Portfolio portfolio) {

    LOG.info("Saving portfolio {}", portfolio);

    PortfolioDO portfolioDO = PortfolioMapper.INSTANCE.portfolioToPortfolioDO(portfolio);
    portfolioRepository.save(portfolioDO);

    for (ConstituentDO constituent: portfolioDO.getConstituents()) {
      constituent.getId().setPortfolioId(portfolioDO.getId());
      constituentDORepository.save(constituent);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void removePortfolio(Portfolio portfolio) {
    portfolioRepository.delete(PortfolioMapper.INSTANCE.portfolioToPortfolioDO(portfolio));
  }

  @Override
  public Collection<HasPrice> getAllPriceableInstruments() {
    List<InstrumentDO> stocks = instrumentRepositry.findByInstrumentType(InstrumentType.STOCK.toString());
    List<InstrumentDO> etfs = instrumentRepositry.findByInstrumentType(InstrumentType.EXCHANGE_TRADED_FUND.toString());
    return Streams.concat(stocks.stream(), etfs.stream()).map(PortfolioMapper.INSTANCE::toCore)
        .map(HasPrice.class::cast).collect(Collectors.toList());
  }

  @Override
  public void newPortfolio(String name) {
    Portfolio newPortfolio = new Portfolio.Builder().withName(name).build();
    portfolioRepository.save(PortfolioMapper.INSTANCE.portfolioToPortfolioDO(newPortfolio));
  }

}
