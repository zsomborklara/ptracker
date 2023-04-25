package hu.zsomboro.ptracker.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import hu.zsomboro.ptracker.service.PriceLoaderService;

@EnableScheduling
public class ClosingPriceLoadingScheduler {

  private static final Logger LOG = LoggerFactory.getLogger(ClosingPriceLoadingScheduler.class);

  private final PriceLoaderService priceLoaderService;

  public ClosingPriceLoadingScheduler(PriceLoaderService priceLoaderService) {
    super();
    this.priceLoaderService = priceLoaderService;
  }

  @Scheduled(cron = "${price.load.cron}")
  public void loadPricesAndFxRates() {

    LOG.info("Daily scheduled price loading starting...");
    priceLoaderService.fetchPrices();
    LOG.info("Daily scheduled price loading end");
  }
}
