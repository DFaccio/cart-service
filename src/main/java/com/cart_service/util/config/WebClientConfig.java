package com.cart_service.util.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("webbuilder")
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {

        return WebClient.builder();

    }

    @Bean("webclient")
    @LoadBalanced
    public WebClient getWebClient() {

        return WebClient.create();

    }


}
