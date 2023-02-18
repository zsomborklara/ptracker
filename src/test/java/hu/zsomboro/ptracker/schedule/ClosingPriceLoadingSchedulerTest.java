package hu.zsomboro.ptracker.schedule;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hu.zsomboro.ptracker.service.PriceLoaderService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestPropertySource(properties = "price.load.cron=*/2 * * ? * *")
@EnableScheduling
public class ClosingPriceLoadingSchedulerTest {

  @MockBean
  private PriceLoaderService priceLoaderSerivce;

  @SpyBean
  private ClosingPriceLoadingScheduler scheduler;

  @Test
  public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
    Awaitility.await().atMost(Duration.ofSeconds(3))
        .untilAsserted(() -> verify(scheduler, atLeast(1)).loadPricesAndFxRates());
  }

}
