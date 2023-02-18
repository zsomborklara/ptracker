package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import hu.zsomboro.ptracker.client.FxRateClient;
import hu.zsomboro.ptracker.client.PriceClient;
import hu.zsomboro.ptracker.common.CoreToPersistenceMapper;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.persistence.HufFxRateDORepository;
import hu.zsomboro.ptracker.persistence.PriceDORepository;
import hu.zsomboro.ptracker.persistence.entity.HufFxRateDO;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;

@Service
public class PriceLoaderServiceImpl implements PriceLoaderService {

  private final PriceDORepository priceRepository;
  private final HufFxRateDORepository fxRateRepository;
  private final CoreToPersistenceMapper mapper;
  private final PortfolioService portfolioService;
  private final PriceClient priceClient;
  private final FxRateClient fxRateClient;

  public PriceLoaderServiceImpl(PriceDORepository priceRepository, HufFxRateDORepository fxRateRepository,
      CoreToPersistenceMapper mapper, PortfolioService portfolioService, PriceClient priceClient,
      FxRateClient fxRateClient) {
    super();
    this.priceRepository = priceRepository;
    this.fxRateRepository = fxRateRepository;
    this.mapper = mapper;
    this.portfolioService = portfolioService;
    this.priceClient = priceClient;
    this.fxRateClient = fxRateClient;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void fetchPrices() {

    Collection<HasPrice> priceableInstruments = portfolioService.getAllPriceableInstruments();

    List<PriceDO> fetchedPrices = Lists.newArrayList();
    Map<String, HufFxRateDO> fetchedFxRates = Maps.newHashMap();

    for (HasPrice priceableInstrument : priceableInstruments) {
      Price todayPrice = priceClient.getTodayPrice(priceableInstrument.getIdentifier());
      PriceDO priceDO = mapper.map(todayPrice, PriceDO.class);
      priceDO.setAsOfDate(LocalDate.now());
      priceDO.setIdentifier(priceableInstrument.getIdentifier());
      fetchedPrices.add(priceDO);

      if (!"HUF".equalsIgnoreCase(todayPrice.priceCurrency())
          && !fetchedFxRates.containsKey(todayPrice.priceCurrency())) {
        double rate = fxRateClient.getTodayFxRateToHUF(todayPrice.priceCurrency());
        HufFxRateDO fxRateDO = new HufFxRateDO();
        fxRateDO.setAsOfDate(LocalDate.now());
        fxRateDO.setIsoCurrency(todayPrice.priceCurrency());
        fxRateDO.setValue(rate);
        fetchedFxRates.put(todayPrice.priceCurrency(), fxRateDO);
      }
    }

    priceRepository.saveAll(fetchedPrices);
    fxRateRepository.saveAll(fetchedFxRates.values());
  }

}
