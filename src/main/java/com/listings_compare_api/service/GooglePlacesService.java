package com.listings_compare_api.service;
import com.listings_compare_api.dto.GoogleSearchPlacesBody;
import com.listings_compare_api.dto.GoogleSearchResponseDTO;
import com.listings_compare_api.ConfigDebug;
import com.listings_compare_api.config.GooglePlacesConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GooglePlacesService {
        private static final Logger log = LoggerFactory.getLogger(ConfigDebug.class);
        private final @Qualifier("googlePlacesRestClient") RestClient restClient;
    // private final GooglePlacesConfig config;

public GooglePlacesService(@Qualifier("googlePlacesRestClient") RestClient client,
        GooglePlacesConfig config) {
    this.restClient = client;
    // this.config = config;
}

    public static String ping() {
        return "ça marche";
    }


        @Cacheable(
      cacheNames = "placesByName",
        key = "'v1:' + #name.trim().toLowerCase().replaceAll('\\s+', ' ')"
  )
    public GoogleSearchResponseDTO getPlacesData(String name) {
        GoogleSearchResponseDTO response =
    this.restClient.post()
        .uri("/v1/places:searchText")
        .header("X-Goog-FieldMask",
            "places.id,places.displayName,places.formattedAddress")
        .body(new GoogleSearchPlacesBody(name))
        .retrieve()
        .body(GoogleSearchResponseDTO.class);

        return response;
    }

    public void debugRequest(String name) {
        var entity =
    restClient.post()
        .uri("/v1/places:searchText")
        .header("X-Goog-FieldMask",
            "places.id,places.displayName,places.formattedAddress")
        .body(new GoogleSearchPlacesBody(name))
        .retrieve()
        .toEntity(String.class);

log.info("⬅️ Raw body:\n{}", entity.getBody());

    }
}
