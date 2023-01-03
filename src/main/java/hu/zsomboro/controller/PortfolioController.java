package hu.zsomboro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.zsomboro.core.Portfolio;
import hu.zsomboro.persistence.PersistenceHelperService;

@RestController(value = "portfolio")
public class PortfolioController {

  private PersistenceHelperService persistenceService;

  public PortfolioController(PersistenceHelperService persistenceService) {
    super();
    this.persistenceService = persistenceService;
  }

  @PostMapping(value = "new")
  public void createPortfolio() {
    persistenceService.savePortfolio(Portfolio.EMPTY);
  }

}
