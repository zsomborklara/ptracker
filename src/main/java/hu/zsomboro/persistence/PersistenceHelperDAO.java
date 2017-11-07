package hu.zsomboro.persistence;

public interface PersistenceHelperDAO {

  public PortfolioDO findPortfolio(int portfolioId);

  public void savePortfolio(PortfolioDO portfolio);
}
