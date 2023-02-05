package hu.zsomboro.ptracker.client;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;

@Component
public class YahooPriceClient implements PriceClient, FxRateClient {

  private static final String YAHOO_QUOTE_URL = "https://query2.finance.yahoo.com/v7/finance/quote";
  private static final String SYMBOL = "symbols";
  private static final String FIELDS = "fields";
  private static final String MARKET_PRICE_FIELD = "regularMarketPrice";

  private final RestTemplate restTemplate;

  public YahooPriceClient(RestTemplate restTemplate) {
    super();
    this.restTemplate = restTemplate;
  }

  @Override
  public double getTodayPrice(String identifier) {

    return callAPIWithIdentifierInternal(identifier);
  }

  @Override
  public double getTodayFxRateToHUF(String fromCurrency) {
    return callAPIWithIdentifierInternal(fromCurrency + "HUF=X");
  }

  private double callAPIWithIdentifierInternal(String identifier) {
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(YAHOO_QUOTE_URL).queryParam(SYMBOL, "{" + SYMBOL + "}")
        .queryParam(FIELDS, "{" + FIELDS + "}").encode().toUriString();

    ResponseEntity<YahooPriceResponse> response = this.restTemplate.exchange(urlTemplate, HttpMethod.GET, null,
        YahooPriceResponse.class, Map.of(SYMBOL, identifier, FIELDS, MARKET_PRICE_FIELD));
    YahooPriceResponse body = response.getBody();

    String error = body.getQuoteResponse().getError();
    if (!Strings.isNullOrEmpty(error)) {
      throw new IllegalStateException("Cannot get prices from yahoo finance: " + error);
    }
    return body.getMarketPrice();
  }
}
