package hu.zsomboro.ptracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.zsomboro.ptracker.client.FxRateClient;
import hu.zsomboro.ptracker.client.PriceClient;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;

@DataJpaTest
@ContextConfiguration
public class PortfolioServiceImplTest {

  @Autowired
  private PortfolioServiceImpl portfolioService;

  @Test
  public void testSavingExistingPortfolio_ResultsInUpdate() {
    EquitySecurity stock = EquitySecurity.newStock("dummy", "DMY");
    Portfolio portfolio = new Portfolio.Builder().withName("TEST").add(stock, 10).build();

    portfolioService.savePortfolio(portfolio);

    Portfolio loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents(), hasSize(1));
    assertTrue(loadedPortfolio.hasInstrument(stock));

    FixedIncomeSecurity deposit = FixedIncomeSecurity.newDeposit("Deposit", "DP1", LocalDate.now(), 2);
    Portfolio updatedPortfolio = portfolio.withInstrument(deposit, 100);

    portfolioService.savePortfolio(updatedPortfolio);

    loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents(), hasSize(2));
    assertTrue(loadedPortfolio.hasInstrument(stock));
    assertTrue(loadedPortfolio.hasInstrument(deposit));
  }

  @Test
  public void testAddingSameInstrumentToExistingPortfolio_ResultsInUpdate() {
    EquitySecurity stock = EquitySecurity.newStock("dummy", "DMY");
    Portfolio portfolio = new Portfolio.Builder().withName("TEST").add(stock, 10).build();

    portfolioService.savePortfolio(portfolio);

    Portfolio loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents(), hasSize(1));
    assertTrue(loadedPortfolio.hasInstrument(stock));

    Portfolio updatedPortfolio = portfolio.withInstrument(stock, 100);

    portfolioService.savePortfolio(updatedPortfolio);

    loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents(), hasSize(1));
    assertTrue(loadedPortfolio.hasInstrument(stock));
    assertThat(loadedPortfolio.getConstituent(stock).getNumber(), equalTo(110));
  }

  @Configuration
  @EnableAutoConfiguration
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.common", "hu.zsomboro.ptracker.service",
      "hu.zsomboro.ptracker.persistence" })
  public static class SpringServiceTestConfig {

    @Bean
    public PriceClient mockPriceClient() {
      return mock(PriceClient.class);
    }

    @Bean
    public FxRateClient mockFxClient() {
      return mock(FxRateClient.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

  }

}
