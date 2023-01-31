package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.zsomboro.ptracker.common.CoreToPersistenceMapper;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.HasPrice;
import hu.zsomboro.ptracker.persistence.PriceDORepository;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import hu.zsomboro.ptracker.persistence.entity.PriceId;

@Service
public class StockPriceServiceImpl implements PriceService {

  private final PriceDORepository repository;
  private final CoreToPersistenceMapper mapper;

  public StockPriceServiceImpl(PriceDORepository repository, CoreToPersistenceMapper mapper) {
    super();
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Price getPrice(LocalDate asOfDate, HasPrice pricedInstrument) {
    Optional<PriceDO> foundPriceOp = repository.findById(new PriceId(pricedInstrument.getIdentifier(), asOfDate));
    return foundPriceOp.map(p -> mapper.map(p, Price.class)).orElse(null);
  }

  @Override
  public Map<LocalDate, Price> getPriceHistory(HasPrice pricedInstrument) {
    List<PriceDO> allPricesForId = repository.findByIdentifier(pricedInstrument.getIdentifier());
    return allPricesForId.stream().collect(Collectors.toMap(PriceDO::getAsOfDate, p -> mapper.map(p, Price.class)));
  }

  @Override
  public Map<String, Price> getAllPricesForDay(LocalDate asOfDate) {
    List<PriceDO> allPricesForDate = repository.findByAsOfDate(asOfDate);
    return allPricesForDate.stream().collect(Collectors.toMap(PriceDO::getIdentifier, p -> mapper.map(p, Price.class)));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void savePirce(LocalDate asOfDate, HasPrice pricedInstrument, Price price) {
    PriceDO priceDO = mapper.map(price, PriceDO.class);
    priceDO.setAsOfDate(asOfDate);
    priceDO.setIdentifier(pricedInstrument.getIdentifier());
    repository.save(priceDO);
  }

}
