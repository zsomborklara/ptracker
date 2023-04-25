package hu.zsomboro.ptracker.client;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;

import hu.zsomboro.ptracker.client.YahooFinanceAPIResponse.Result;
import hu.zsomboro.ptracker.core.Price;

@Component
public class YahooPriceClient implements PriceClient, FxRateClient {

  private static final Logger LOG = LoggerFactory.getLogger(YahooPriceClient.class);
  private static final String YAHOO_QUOTE_URL = "https://query2.finance.yahoo.com/v7/finance/quote";
  private static final String SYMBOL = "symbols";
  private static final String FIELDS = "fields";
  private static final String REQUESTED_FIELDS = "regularMarketPrice,currency";

  private final RestTemplate restTemplate;

  public YahooPriceClient(RestTemplate restTemplate) {
    super();
    this.restTemplate = restTemplate;
  }

  @Override
  public Price getTodayPrice(String identifier) {
    Result result = callAPIWithIdentifierInternal(identifier);
    LOG.info("Fetching prices for instrument {} at {}", identifier, LocalDate.now());
    return new Price(result.getRegularMarketPrice(), result.getCurrency());
  }

  @Override
  public double getTodayFxRateToHUF(String fromCurrency) {
    Result result = callAPIWithIdentifierInternal(fromCurrency + "HUF=X");
    LOG.info("Fetching HUF fx rates for currency {} at {}", fromCurrency, LocalDate.now());
    return result.getRegularMarketPrice();
  }

  private Result callAPIWithIdentifierInternal(String identifier) {
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(YAHOO_QUOTE_URL).queryParam(SYMBOL, "{" + SYMBOL + "}")
        .queryParam(FIELDS, "{" + FIELDS + "}").encode().toUriString();

    ResponseEntity<YahooFinanceAPIResponse> response = this.restTemplate.exchange(urlTemplate, HttpMethod.GET, null,
        YahooFinanceAPIResponse.class, Map.of(SYMBOL, identifier, FIELDS, REQUESTED_FIELDS));
    YahooFinanceAPIResponse body = response.getBody();

    String error = body.getQuoteResponse().getError();
    if (!Strings.isNullOrEmpty(error)) {
      throw new IllegalStateException("Cannot get prices from yahoo finance: " + error);
    }
    return body.getQuoteResponse().getResult()[0];
  }
}
