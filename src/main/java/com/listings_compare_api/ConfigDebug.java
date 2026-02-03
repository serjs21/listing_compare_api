package com.listings_compare_api;

import com.listings_compare_api.config.GooglePlacesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigDebug {

    private static final Logger log = LoggerFactory.getLogger(ConfigDebug.class);

    @Bean
    ApplicationRunner debug(Environment env, GooglePlacesConfig cfg) {
        return args -> {
            log.info("ENV google-places.base-url = {}", env.getProperty("google-places.base-url"));
            log.info("CFG baseUrl = {}", cfg.baseUrl());
            log.info("CFG apiKey present = {}", cfg.apiKey() != null && !cfg.apiKey().isBlank());
        };
    }
}
