package by.btslogistics.autorelease.service.releasedocument.unloaddocument;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SoapConfig {

    @Value("${url.nased}")
    private String urlNased;

    @Bean(name = "soapWebClient")
    public WebClient soapWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(urlNased).defaultHeader("Content-type", "text/xml").build();
    }
}
