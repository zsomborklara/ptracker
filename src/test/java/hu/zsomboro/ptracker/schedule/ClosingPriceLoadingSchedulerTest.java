package hu.zsomboro.ptracker.schedule;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hu.zsomboro.ptracker.service.PriceLoaderService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClosingPriceLoadingSchedulerTest.TestConfig.class)
@TestPropertySource(properties = "price.load.cron=*/2 * * ? * *")
@EnableScheduling
class ClosingPriceLoadingSchedulerTest {

  @MockitoBean
  private PriceLoaderService priceLoaderService;

  @MockitoSpyBean
  private ClosingPriceLoadingScheduler scheduler;

  @Test
  public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
    Awaitility.await().atMost(Duration.ofSeconds(3))
        .untilAsserted(() -> verify(scheduler, atLeast(1)).loadPricesAndFxRates());
  }

  @Configuration
  static class TestConfig {
    @Bean
    ClosingPriceLoadingScheduler closingPriceLoadingScheduler(PriceLoaderService priceLoaderService) {
      return new ClosingPriceLoadingScheduler(priceLoaderService);
    }
  }
}
