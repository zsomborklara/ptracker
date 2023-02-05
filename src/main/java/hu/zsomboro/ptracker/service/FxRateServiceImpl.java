package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import hu.zsomboro.ptracker.persistence.HufFxRateDORepository;
import hu.zsomboro.ptracker.persistence.entity.FxRateId;
import hu.zsomboro.ptracker.persistence.entity.HufFxRateDO;

@Service
public class FxRateServiceImpl implements FxRateService {

  private final HufFxRateDORepository repository;

  public FxRateServiceImpl(HufFxRateDORepository repository) {
    super();
    this.repository = repository;
  }

  @Override
  public double getHufFxRate(LocalDate asOfDate, String forIsoCurrency) {
    FxRateId id = new FxRateId();
    id.setAsOfDate(asOfDate);
    id.setIsoCurrency(forIsoCurrency);
    return repository.findById(id).orElseThrow().getValue();
  }

  @Override
  public void saveHufFxRate(LocalDate asOfDate, String forIsoCurrency, double value) {
    HufFxRateDO fxRateDO = new HufFxRateDO();
    fxRateDO.setAsOfDate(asOfDate);
    fxRateDO.setIsoCurrency(forIsoCurrency);
    fxRateDO.setValue(value);
    repository.save(fxRateDO);
  }

  @Override
  public Map<String, Double> getAllFxRatesForDay(LocalDate asOfDate) {
    List<HufFxRateDO> fxRatesByDate = repository.findByAsOfDate(asOfDate);
    return fxRatesByDate.stream().collect(Collectors.toMap(HufFxRateDO::getIsoCurrency, HufFxRateDO::getValue));
  }

}
