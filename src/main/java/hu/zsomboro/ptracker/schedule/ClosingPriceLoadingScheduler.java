package hu.zsomboro.ptracker.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import hu.zsomboro.ptracker.service.PriceLoaderService;

@EnableScheduling
public class ClosingPriceLoadingScheduler {

  private final PriceLoaderService priceLoaderService;

  public ClosingPriceLoadingScheduler(PriceLoaderService priceLoaderService) {
    super();
    this.priceLoaderService = priceLoaderService;
  }

  @Scheduled(cron = "${price.load.cron}")
  public void loadPricesAndFxRates() {
    priceLoaderService.fetchPrices();
  }
}
