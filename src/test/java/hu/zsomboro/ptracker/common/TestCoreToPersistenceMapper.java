package hu.zsomboro.ptracker.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.Portfolio.Constituent;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class TestCoreToPersistenceMapper {

  @Autowired
  private CoreToPersistenceMapper mapper;

  @Test
  public void whenPriceMatch_thenConvertsToDTO() {

    Price price = new Price(123.4d, "HUF");
    PriceDO priceDTO = this.mapper.map(price, PriceDO.class);

    assertEquals(price.value(), priceDTO.getPrice());
    assertEquals(price.priceCurrency(), priceDTO.getCurrency());
  }

  @Test
  public void whenPriceDOMatch_thenConvertsToCore() {

    PriceDO priceDTO = new PriceDO();
    priceDTO.setCurrency("USD");
    priceDTO.setIdentifier("dummy");
    priceDTO.setAsOfDate(LocalDate.now());
    Price price = this.mapper.map(priceDTO, Price.class);

    assertEquals(price.value(), priceDTO.getPrice());
    assertEquals(price.priceCurrency(), priceDTO.getCurrency());
  }

  @Test
  public void whenInstrumentMatch_thenConvertsToDTO() {

    EquitySecurity instrument = EquitySecurity.newStock("dummy", "1112233DD");
    EquitySecurityDO instrumentDTO = this.mapper.map(instrument, EquitySecurityDO.class);

    assertEquals(instrument.getName(), instrumentDTO.getName());
    assertEquals(instrument.getIdentifier(), instrumentDTO.getIdentifier());
    assertEquals(instrument.getInstrumentType().toString(), instrumentDTO.getInstrumentType());

    FixedIncomeSecurity instrument2 = FixedIncomeSecurity.newTBond("dummy", "1112233DD", LocalDate.now(), 12.3d);
    FixedIncomeSecurityDO instrumentDTO2 = this.mapper.map(instrument2, FixedIncomeSecurityDO.class);

    assertEquals(instrument2.getName(), instrumentDTO2.getName());
    assertEquals(instrument2.getIdentifier(), instrumentDTO2.getIdentifier());
    assertEquals(instrument2.getInstrumentType().toString(), instrumentDTO2.getInstrumentType());
    assertEquals(instrument2.getMaturity(), instrumentDTO2.getMaturity());
    assertEquals(instrument2.getInterestRate(), instrumentDTO2.getInterestRate());
  }

  @Test
  public void whenInstrumentDOMatch_thenConvertsToCore() {

    EquitySecurityDO instrumentDTO = new EquitySecurityDO();
    instrumentDTO.setIdentifier("abc1122");
    instrumentDTO.setInstrumentType("STOCK");
    instrumentDTO.setName("Dummy");
    EquitySecurity instrument = this.mapper.map(instrumentDTO, EquitySecurity.class);

    assertEquals(instrument.getName(), instrumentDTO.getName());
    assertEquals(instrument.getIdentifier(), instrumentDTO.getIdentifier());
    assertEquals(instrument.getInstrumentType().toString(), instrumentDTO.getInstrumentType());

    FixedIncomeSecurityDO instrumentDTO2 = new FixedIncomeSecurityDO();
    instrumentDTO2.setIdentifier("cde33222");
    instrumentDTO2.setInstrumentType("PENSION_FUND");
    instrumentDTO2.setInterestRate(123.5d);
    instrumentDTO2.setMaturity(LocalDate.now());
    instrumentDTO2.setName("Dummy2");
    FixedIncomeSecurity instrument2 = this.mapper.map(instrumentDTO2, FixedIncomeSecurity.class);

    assertEquals(instrument2.getName(), instrumentDTO2.getName());
    assertEquals(instrument2.getIdentifier(), instrumentDTO2.getIdentifier());
    assertEquals(instrument2.getInstrumentType().toString(), instrumentDTO2.getInstrumentType());
    assertEquals(instrument2.getMaturity(), instrumentDTO2.getMaturity());
    assertEquals(instrument2.getInterestRate(), instrumentDTO2.getInterestRate());

  }

  @Test
  public void whenPortfolioMatch_thenConvertsToDTO() {

    String firstId = "1112233DD";
    EquitySecurity instrument = EquitySecurity.newStock("sec", firstId);
    String secondId = "1112233FF";
    FixedIncomeSecurity instrument2 = FixedIncomeSecurity.newTBond("tbond", secondId, LocalDate.now(), 12.3d);

    Portfolio portfolio = new Portfolio(Map.of(instrument, 1, instrument2, 3), new Cash(123.d, "HUF"), "Dummy");
    PortfolioDO portfolioDTO = this.mapper.map(portfolio, PortfolioDO.class);

    assertEquals(portfolio.getName(), portfolioDTO.getName());
    assertEquals(portfolio.getCash().currency(), portfolioDTO.getCash().getCurrency());
    assertEquals(portfolio.getCash().amount(), portfolioDTO.getCash().getAmount());
    Iterator<ConstituentDO> doConstituentIterator = portfolioDTO.getConstituents().iterator();
    ConstituentDO first = null;
    ConstituentDO second = null;
    while (doConstituentIterator.hasNext()) {
      ConstituentDO next = doConstituentIterator.next();
      if (next.getInstrument().getIdentifier().equals(firstId)) {
        first = next;
      } else if (next.getInstrument().getIdentifier().equals(secondId)) {
        second = next;
      }
    }

    assertEquals(portfolio.getConstituent(instrument).getNumber(), first.getNumber());
    assertEquals(portfolio.getConstituent(instrument).getInstrument().getIdentifier(),
        first.getInstrument().getIdentifier());
    assertEquals(portfolio.getConstituent(instrument).getInstrument().getInstrumentType().toString(),
        first.getInstrument().getInstrumentType());

    assertEquals(portfolio.getConstituent(instrument2).getNumber(), second.getNumber());
    assertEquals(portfolio.getConstituent(instrument2).getInstrument().getIdentifier(),
        second.getInstrument().getIdentifier());
    assertEquals(portfolio.getConstituent(instrument2).getInstrument().getInstrumentType().toString(),
        second.getInstrument().getInstrumentType());
    assertEquals(((FixedIncomeSecurity) portfolio.getConstituent(instrument2).getInstrument()).getInterestRate(),
        ((FixedIncomeSecurityDO) second.getInstrument()).getInterestRate());
    assertEquals(((FixedIncomeSecurity) portfolio.getConstituent(instrument2).getInstrument()).getMaturity(),
        ((FixedIncomeSecurityDO) second.getInstrument()).getMaturity());
  }

  @Test
  public void whenPortfolioDOMatch_thenConvertsToCore() {

    EquitySecurityDO instrumentDTO = new EquitySecurityDO();
    String firstId = "abc1122";
    instrumentDTO.setIdentifier(firstId);
    instrumentDTO.setInstrumentType("STOCK");
    instrumentDTO.setName("Dummy");

    FixedIncomeSecurityDO instrumentDTO2 = new FixedIncomeSecurityDO();
    String secondId = "cde33222";
    instrumentDTO2.setIdentifier(secondId);
    instrumentDTO2.setInstrumentType("PENSION_FUND");
    instrumentDTO2.setInterestRate(123.5d);
    instrumentDTO2.setMaturity(LocalDate.now());
    instrumentDTO2.setName("Dummy2");

    PortfolioDO portfolioDTO = new PortfolioDO();
    portfolioDTO.setCash(new CashDO("EUR", 12233.d));
    portfolioDTO.setName("DummyPortfolio");
    portfolioDTO.setConstituents(Set.of(new ConstituentDO(instrumentDTO, 100), new ConstituentDO(instrumentDTO2, 200)));
    Portfolio portfolio = this.mapper.map(portfolioDTO, Portfolio.class);

    assertEquals(portfolio.getName(), portfolioDTO.getName());
    assertEquals(portfolio.getCash().currency(), portfolioDTO.getCash().getCurrency());
    assertEquals(portfolio.getCash().amount(), portfolioDTO.getCash().getAmount());
    Iterator<Constituent> constituentIterator = portfolio.getConstituents().iterator();
    Constituent first = null;
    Constituent second = null;
    while (constituentIterator.hasNext()) {
      Constituent next = constituentIterator.next();
      if (next.getInstrument().getIdentifier().equals(firstId)) {
        first = next;
      } else if (next.getInstrument().getIdentifier().equals(secondId)) {
        second = next;
      }
    }

    Iterator<ConstituentDO> doConstituentIterator = portfolioDTO.getConstituents().iterator();
    ConstituentDO firstDO = null;
    ConstituentDO secondDO = null;
    while (doConstituentIterator.hasNext()) {
      ConstituentDO next = doConstituentIterator.next();
      if (next.getInstrument().getIdentifier().equals(firstId)) {
        firstDO = next;
      } else if (next.getInstrument().getIdentifier().equals(secondId)) {
        secondDO = next;
      }
    }

    assertEquals(first.getNumber(), firstDO.getNumber());
    assertEquals(first.getInstrument().getIdentifier(), firstDO.getInstrument().getIdentifier());
    assertEquals(first.getInstrument().getInstrumentType().toString(), firstDO.getInstrument().getInstrumentType());

    assertEquals(second.getNumber(), secondDO.getNumber());
    assertEquals(second.getInstrument().getIdentifier(), secondDO.getInstrument().getIdentifier());
    assertEquals(second.getInstrument().getInstrumentType().toString(), secondDO.getInstrument().getInstrumentType());
    assertEquals(((FixedIncomeSecurity) second.getInstrument()).getInterestRate(),
        ((FixedIncomeSecurityDO) secondDO.getInstrument()).getInterestRate());
    assertEquals(((FixedIncomeSecurity) second.getInstrument()).getMaturity(),
        ((FixedIncomeSecurityDO) secondDO.getInstrument()).getMaturity());
  }

  @Configuration
  @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.common" })
  public static class SpringTestConfig {

  }
}
