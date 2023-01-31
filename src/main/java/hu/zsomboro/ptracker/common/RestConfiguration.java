package hu.zsomboro.ptracker.common;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestConfiguration {

  @Bean
  public HttpClient apacheHttpClient() {
    return HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
  }

  @Bean
  public RestTemplate restTemplate(HttpClient httpClient, ObjectMapper mapper) {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(mapper);
    RestTemplate restTemplate = new RestTemplate(requestFactory);
    restTemplate.setMessageConverters(List.of(converter));
    return restTemplate;
  }

}
