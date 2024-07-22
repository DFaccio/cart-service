package com.cart_service.util.config;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${product.address}")
    private String productAddress;

    @Bean
    public WebClient webClientProdutos(WebClient.Builder builder) {
        return builder
                .baseUrl(productAddress)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("webClientBuilder")
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {

        return WebClient.builder();

    }

    @Bean("webclient")
    @LoadBalanced
    public WebClient getWebClient() {

        return WebClient.create();

    }


}
