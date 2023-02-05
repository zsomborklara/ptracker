package hu.zsomboro.ptracker.service;

import java.time.LocalDate;
import java.util.Map;

public interface FxRateService {

  public double getHufFxRate(LocalDate asOfDate, String forIsoCurrency);

  public void saveHufFxRate(LocalDate asOfDate, String forIsoCurrency, double value);

  public Map<String, Double> getAllFxRatesForDay(LocalDate asOfDate);

}
