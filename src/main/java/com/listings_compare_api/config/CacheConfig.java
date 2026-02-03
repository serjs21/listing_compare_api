package com.listings_compare_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
public record CacheConfig(int placeIdTtlDays, int reviewsTtlDays, int negativeTtlDays) {}
