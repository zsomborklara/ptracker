package hu.zsomboro.ptracker.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class TestYahooPriceClient {

  @Autowired
  private PriceClient priceClient;

  @Autowired
  private RestTemplate restTemplate;

  private MockRestServiceServer server;

  @BeforeEach
  public void init() throws JsonMappingException, JsonProcessingException {
    server = MockRestServiceServer.bindTo(restTemplate).build();
    String response = "{\"quoteResponse\":{\"result\":[{\"language\":\"en-US\",\"region\":\"US\",\"quoteType\":\"EQUITY\",\"typeDisp\":\"Equity\",\"quoteSourceName\":\"Delayed Quote\",\"triggerable\":true,\"customPriceAlertConfidence\":\"HIGH\",\"regularMarketPrice\":506.11,\"gmtOffSetMilliseconds\":-18000000,\"market\":\"us_market\",\"esgPopulated\":false,\"exchange\":\"NYQ\",\"exchangeTimezoneName\":\"America/New_York\",\"exchangeTimezoneShortName\":\"EST\",\"marketState\":\"POST\",\"firstTradeDateMilliseconds\":1195137000000,\"priceHint\":2,\"regularMarketTime\":1675112395,\"fullExchangeName\":\"NYSE\",\"sourceInterval\":15,\"exchangeDataDelayedBy\":0,\"tradeable\":false,\"cryptoTradeable\":false,\"symbol\":\"IBM\"}],\"error\":null}}";
    this.server
        .expect(requestTo("https://query2.finance.yahoo.com/v7/finance/quote?symbols=IBM&fields=regularMarketPrice"))
        .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
  }

  @Test
  public void testCallYahooService() {
    double todayPrice = priceClient.getTodayPrice("IBM");
    assertThat(todayPrice, closeTo(506.11d, 1e-14));
  }

  @Configuration
  @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
  @ComponentScan(basePackages = { "hu.zsomboro.ptracker.client", "hu.zsomboro.ptracker.common" })
  public static class SpringClientTestConfig {

  }
}
