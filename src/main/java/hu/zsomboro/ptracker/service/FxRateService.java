package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

public interface FxRateService {

  double getHufFxRate(LocalDate asOfDate, String forIsoCurrency);

  void saveHufFxRate(LocalDate asOfDate, String forIsoCurrency, double value);

  Map<String, Double> getAllFxRatesForDay(LocalDate asOfDate);

}
