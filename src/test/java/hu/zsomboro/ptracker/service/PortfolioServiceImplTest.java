package hu.zsomboro.ptracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Collection;

import hu.zsomboro.ptracker.core.security.HasPrice;
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
    assertThat(loadedPortfolio.getConstituents()).hasSize(1);
    assertThat(loadedPortfolio.hasInstrument(stock)).isTrue();

    FixedIncomeSecurity deposit = FixedIncomeSecurity.newDeposit("Deposit", "DP1", LocalDate.now(), 2);
    Portfolio updatedPortfolio = loadedPortfolio.withInstrument(deposit, 100);

    portfolioService.savePortfolio(updatedPortfolio);

    loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents()).hasSize(2);
    assertThat(loadedPortfolio.hasInstrument(stock)).isTrue();
    assertThat(loadedPortfolio.hasInstrument(deposit)).isTrue();
  }

  @Test
  public void testAddingSameInstrumentToExistingPortfolio_ResultsInUpdate() {
    EquitySecurity stock = EquitySecurity.newStock("dummy", "DMY");
    Portfolio portfolio = new Portfolio.Builder().withName("TEST").add(stock, 10).build();

    portfolioService.savePortfolio(portfolio);

    Portfolio loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents()).hasSize(1);
    assertThat(loadedPortfolio.hasInstrument(stock)).isTrue();

    Portfolio updatedPortfolio = loadedPortfolio.withInstrument(stock, 100);

    portfolioService.savePortfolio(updatedPortfolio);

    loadedPortfolio = portfolioService.findPortfolio("TEST");
    assertThat(loadedPortfolio.getConstituents()).hasSize(1);
    assertThat(loadedPortfolio.hasInstrument(stock)).isTrue();
    assertThat(loadedPortfolio.getConstituent(stock).number()).isEqualTo(110);
  }

  @Test
  public void testCanFindAllPricableInstruments() {
    EquitySecurity stock = EquitySecurity.newStock("dummyStock", "DMYTCK1");
    Portfolio portfolio = new Portfolio.Builder().withName("TEST1").add(stock, 10).build();

    portfolioService.savePortfolio(portfolio);

    EquitySecurity fund = EquitySecurity.newETF("dummyEtf", "DMYTCK2");
    Portfolio otherPortfolio = new Portfolio.Builder().withName("TEST2").add(fund, 10).build();


    portfolioService.savePortfolio(otherPortfolio);

    Collection<HasPrice> allPricableInstruments = portfolioService.getAllPriceableInstruments();
    assertThat(allPricableInstruments).hasSize(2);
    assertThat(allPricableInstruments).contains(stock, fund);

  }

  @Test
  public void testCanDeletePortfolio() {
    EquitySecurity stock = EquitySecurity.newStock("dummyStock", "DMYTCK1");
    Portfolio portfolio = new Portfolio.Builder().withName("TEST1").add(stock, 10).build();

    portfolioService.savePortfolio(portfolio);

    Portfolio loadedPortfolio = portfolioService.findPortfolio("TEST1");

    portfolioService.removePortfolio(loadedPortfolio);

    loadedPortfolio = portfolioService.findPortfolio("TEST1");
    assertThat(loadedPortfolio).isSameAs(Portfolio.EMPTY);
  }

  @Test
  public void testDeleteEmptyPortfolio_NoException() {
    portfolioService.removePortfolio(Portfolio.EMPTY);
    Portfolio portfolio = portfolioService.findPortfolio("IdontExist");
    assertThat(portfolio).isSameAs(Portfolio.EMPTY);
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
