package hu.zsomboro.ptracker.client;

public class YahooFxRateResponse {

  private QuoteResponse quoteResponse;

  public QuoteResponse getQuoteResponse() {
    return quoteResponse;
  }

  public void setQuoteResponse(QuoteResponse quoteResponse) {
    this.quoteResponse = quoteResponse;
  }

  public Double getMarketPrice() {
    return quoteResponse.result[0].regularMarketPrice;
  }

  public static class QuoteResponse {
    private Result[] result;
    private String error;

    public Result[] getResult() {
      return result;
    }

    public void setResult(Result[] result) {
      this.result = result;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }
  }

  public static class Result {
    private String fullExchangeName;
    private String exchangeTimezoneName;
    private String symbol;
    private String quoteType;
    private Double regularMarketPrice;

    public String getFullExchangeName() {
      return fullExchangeName;
    }

    public void setFullExchangeName(String fullExchangeName) {
      this.fullExchangeName = fullExchangeName;
    }

    public String getExchangeTimezoneName() {
      return exchangeTimezoneName;
    }

    public void setExchangeTimezoneName(String exchangeTimezoneName) {
      this.exchangeTimezoneName = exchangeTimezoneName;
    }

    public String getSymbol() {
      return symbol;
    }

    public void setSymbol(String symbol) {
      this.symbol = symbol;
    }

    public String getQuoteType() {
      return quoteType;
    }

    public void setQuoteType(String quoteType) {
      this.quoteType = quoteType;
    }

    public Double getRegularMarketPrice() {
      return regularMarketPrice;
    }

    public void setRegularMarketPrice(Double regularMarketPrice) {
      this.regularMarketPrice = regularMarketPrice;
    }

  }

}
