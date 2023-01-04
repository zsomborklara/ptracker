package hu.zsomboro.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hu.zsomboro.core.Portfolio;
import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.persistence.PersistenceHelperService;

@RestController
@RequestMapping(value = "portfolio")
public class PortfolioController {

  private PersistenceHelperService persistenceService;

  public PortfolioController(PersistenceHelperService persistenceService) {
    super();
    this.persistenceService = persistenceService;
  }

  @PostMapping(value = "{name}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void createPortfolio(@PathVariable String name) {
    persistenceService.newPortfolio(name);
  }

  @GetMapping(value = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Portfolio findPortfolio(@PathVariable String name) {
    return persistenceService.findPortfolio(name);
  }

  @PostMapping(value = "{name}/stock/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addStock(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Instrument instrument = EquitySecurity.newStock(data.get("name"), id);
    addInstrument(name, amount, instrument);
  }

  @PostMapping(value = "{name}/etf/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addETF(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Instrument instrument = EquitySecurity.newETF(data.get("name"), id);
    addInstrument(name, amount, instrument);
  }

  @PostMapping(value = "{name}/deposit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addDeposit(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Double interestRate = Double.valueOf(data.get("interestRate"));
    LocalDate maturity = LocalDate.parse(data.get("maturity"));
    Instrument instrument = FixedIncomeSecurity.newDeposit(data.get("name"), id, maturity, interestRate);
    addInstrument(name, amount, instrument);
  }

  @PostMapping(value = "{name}/govbond/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addGovernmentBond(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Double interestRate = Double.valueOf(data.get("interestRate"));
    LocalDate maturity = LocalDate.parse(data.get("maturity"));
    Instrument instrument = FixedIncomeSecurity.newTBond(data.get("name"), id, maturity, interestRate);
    addInstrument(name, amount, instrument);
  }

  @PostMapping(value = "{name}/pension/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addPensionFund(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Double interestRate = Double.valueOf(data.get("interestRate"));
    LocalDate maturity = LocalDate.parse(data.get("maturity"));
    Instrument instrument = FixedIncomeSecurity.newPensionFund(data.get("name"), id, maturity, interestRate);
    addInstrument(name, amount, instrument);
  }

  private void addInstrument(String name, int amount, Instrument instrument) {
    Portfolio portfolio = persistenceService.findPortfolio(name);
    portfolio.add(instrument, amount);
    persistenceService.savePortfolio(portfolio);
  }

}
