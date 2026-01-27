package com.listings_compare_api.config;

import org.springframework.web.client.RestClient;

import com.listings_compare_api.ConfigDebug;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class HttpConfig {
    private static final Logger log = LoggerFactory.getLogger(ConfigDebug.class);

    @Bean("googlePlacesRestClient")
    RestClient googleRestClient(GooglePlacesConfig config) {
        System.out.println(config.baseUrl());
        System.out.println(config.apiKey());
        
        return RestClient.builder()
            .baseUrl(config.baseUrl())
            .defaultHeader("X-Goog-Api-Key", config.apiKey())
            .defaultHeader("Content-Type", "application/json")
            .requestInterceptor((request, body, execution) -> {
                log.info("➡️ {} {}", request.getMethod(), request.getURI());
                log.info("➡️ Headers: {}", request.getHeaders());
                log.info("➡️ Body: {}", body != null ? new String(body) : "<empty>");

                var response = execution.execute(request, body);

                log.info("⬅️ Status: {}", response.getStatusCode());
                log.info("⬅️ Headers: {}", response.getHeaders());

            return response;
        })
            .build();
    }

    @Bean("bookingRestClient")
    RestClient bookingRestClient() {
        return RestClient.builder()
            .baseUrl("https://api.booking.com")
            .build();
    }
}