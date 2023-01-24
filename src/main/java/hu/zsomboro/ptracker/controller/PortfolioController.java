package hu.zsomboro.ptracker.controller;

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

import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "portfolio")
public class PortfolioController {

  // swagger UI available on: http://localhost:8080/tracker/swagger-ui/index.html
  private PortfolioService persistenceService;

  public PortfolioController(PortfolioService persistenceService) {
    super();
    this.persistenceService = persistenceService;
  }

  @Operation(summary = "Create a new portfolio with the given name")
  @ApiResponse(responseCode = "204", description = "Portfolio created, no content", content = { @Content })
  @PostMapping(value = "{name}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void createPortfolio(@PathVariable String name) {
    persistenceService.newPortfolio(name);
  }

  @Operation(summary = "Return the portfolio by name. If it does not exist yet, return an empty portfolio")
  @ApiResponse(responseCode = "200", description = "Portfolio with the give name", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = Portfolio.class)) })
  @GetMapping(value = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Portfolio findPortfolio(@PathVariable String name) {
    return persistenceService.findPortfolio(name);
  }

  @Operation(summary = "Add a stock security to the portfolio")
  @ApiResponse(responseCode = "204", description = "Portfolio updated, no content", content = { @Content })
  @PostMapping(value = "{name}/stock/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addStock(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Instrument instrument = EquitySecurity.newStock(data.get("name"), id);
    addInstrument(name, amount, instrument);
  }

  @Operation(summary = "Add an exchange traded fund to the portfolio")
  @ApiResponse(responseCode = "204", description = "Portfolio updated, no content", content = { @Content })
  @PostMapping(value = "{name}/etf/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addETF(@PathVariable String name, @PathVariable String instrumentType, @PathVariable String id,
      @RequestBody Map<String, String> data) {
    Integer amount = Integer.valueOf(data.get("amount"));
    Instrument instrument = EquitySecurity.newETF(data.get("name"), id);
    addInstrument(name, amount, instrument);
  }

  @Operation(summary = "Add a bank deposit security to the portfolio")
  @ApiResponse(responseCode = "204", description = "Portfolio updated, no content", content = { @Content })
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

  @Operation(summary = "Add a government bond to the portfolio")
  @ApiResponse(responseCode = "204", description = "Portfolio updated, no content", content = { @Content })
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

  @Operation(summary = "Add a pension fund security to the portfolio")
  @ApiResponse(responseCode = "204", description = "Portfolio updated, no content", content = { @Content })
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
