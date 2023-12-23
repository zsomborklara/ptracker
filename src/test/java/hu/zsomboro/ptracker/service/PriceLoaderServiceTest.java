package hu.zsomboro.ptracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hu.zsomboro.ptracker.client.FxRateClient;
import hu.zsomboro.ptracker.client.PriceClient;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.persistence.HufFxRateDORepository;
import hu.zsomboro.ptracker.persistence.PriceDORepository;
import hu.zsomboro.ptracker.persistence.entity.HufFxRateDO;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class PriceLoaderServiceTest {

  @MockBean
  private PriceDORepository priceRepository;
  @MockBean
  private HufFxRateDORepository fxRateRepository;
  @MockBean
  private PortfolioService portfolioService;
  @MockBean
  private PriceClient priceClient;
  @MockBean
  private FxRateClient fxRateClient;
  @Autowired
  private PriceLoaderService priceLoaderService;
  @Captor
  private ArgumentCaptor<Iterable<?>> collectionCaptor;

  @SuppressWarnings("unchecked")
  @Test
  public void testPriceLoadingForInstruments() {

    String id1 = "id1";
    String id2 = "id2";
    String id3 = "id3";
    LocalDate today = LocalDate.now();
    when(portfolioService.getAllPriceableInstruments()).thenReturn(List.of(EquitySecurity.newStock("dummy1", id1),
        EquitySecurity.newStock("dummy2", id2), EquitySecurity.newStock("dummy3", id3)));
    when(priceClient.getTodayPrice(id1)).thenReturn(new Price(2.d, "HUF"));
    when(priceClient.getTodayPrice(id2)).thenReturn(new Price(3.d, "USD"));
    when(priceClient.getTodayPrice(id3)).thenReturn(new Price(4.d, "USD"));
    when(fxRateClient.getTodayFxRateToHUF("USD")).thenReturn(234.d);

    priceLoaderService.fetchPrices();
    verify(priceClient, times(3)).getTodayPrice(anyString());
    verify(fxRateClient, times(1)).getTodayFxRateToHUF("USD");
    verify(priceRepository, times(1)).saveAll((Iterable<PriceDO>) collectionCaptor.capture());
    Iterator<?> capturedPrices = collectionCaptor.getValue().iterator();
    PriceDO firstPriceDO = (PriceDO) capturedPrices.next();
    assertThat(firstPriceDO.getIdentifier()).isEqualTo("id1");
    assertThat(firstPriceDO.getPrice()).isCloseTo(2.d, Offset.offset(1e-14));
    assertThat(firstPriceDO.getAsOfDate()).isEqualTo(today);

    PriceDO secondPriceDO = (PriceDO) capturedPrices.next();
    assertThat(secondPriceDO.getIdentifier()).isEqualTo("id2");

    PriceDO thirdPriceDO = (PriceDO) capturedPrices.next();
    assertThat(thirdPriceDO.getIdentifier()).isEqualTo("id3");
    assertThat(capturedPrices.hasNext()).isFalse();

    verify(fxRateRepository, times(1)).saveAll((Iterable<HufFxRateDO>) collectionCaptor.capture());
    Iterator<?> capturedFxRate = collectionCaptor.getValue().iterator();
    HufFxRateDO onlyRate = (HufFxRateDO) capturedFxRate.next();
    assertThat(onlyRate.getIsoCurrency()).isEqualTo("USD");
    assertThat(onlyRate.getValue()).isCloseTo(234.d, Offset.offset(1e-14));
    assertThat(onlyRate.getAsOfDate()).isEqualTo(today);

    assertThat(capturedFxRate.hasNext()).isFalse();

  }

  @Configuration
  @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.common", "hu.zsomboro.ptracker.service" })
  public static class SpringServiceTestConfig {

  }

}
