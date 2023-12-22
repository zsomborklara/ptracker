package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import hu.zsomboro.ptracker.common.PriceMapper;
import org.springframework.stereotype.Service;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.persistence.PriceDORepository;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import hu.zsomboro.ptracker.persistence.entity.PriceId;

@Service
public class LoadedPriceServiceImpl implements LoadedPriceService {

  private final PriceDORepository repository;

  public LoadedPriceServiceImpl(PriceDORepository repository) {
    super();
    this.repository = repository;
  }

  @Override
  public Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument) {
    Optional<PriceDO> foundPriceOp = repository.findById(new PriceId(pricedInstrument.getInstrumentId(), asOfDate));
    return foundPriceOp.map(PriceMapper.INSTANCE::priceDOToPrice).orElse(null);
  }

  @Override
  public Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument) {
    List<PriceDO> allPricesForId = repository.findByIdentifier(pricedInstrument.getInstrumentId());
    return allPricesForId.stream().collect(Collectors.toMap(PriceDO::getAsOfDate, PriceMapper.INSTANCE::priceDOToPrice));
  }

  @Override
  public Map<String, Price> getAllPricesForDay(LocalDate asOfDate) {
    List<PriceDO> allPricesForDate = repository.findByAsOfDate(asOfDate);
    return allPricesForDate.stream().collect(Collectors.toMap(PriceDO::getIdentifier, PriceMapper.INSTANCE::priceDOToPrice));
  }

}
