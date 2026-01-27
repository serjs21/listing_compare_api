package com.listings_compare_api.config;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google-places")
public record GooglePlacesConfig(
    String apiKey,
    String baseUrl
) {}