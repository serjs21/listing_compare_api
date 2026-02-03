package com.listings_compare_api.service;

import com.listings_compare_api.ConfigDebug;
import com.listings_compare_api.dto.GoogleSearchPlacesBody;
import com.listings_compare_api.dto.GoogleSearchResponseDTO;
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

  private static String normalizeNameString(String name) {
    return name == null ? "" : name.trim().toLowerCase().replaceAll("\\s+", " ");
  }

  public GooglePlacesService(@Qualifier("googlePlacesRestClient") RestClient client) {
    this.restClient = client;
  }

  public static String ping() {
    return "ça marche";
  }

  @Cacheable(
      cacheNames = "placesByName",
      key =
          "'v1:' + T(com.listings_compare_api.service.GooglePlacesService).normalizeNameString(#name)",
      sync = true)
  public GoogleSearchResponseDTO getPlacesData(String name) {

    if (isNegativeCached(name)) {
      return new GoogleSearchResponseDTO(java.util.List.of());
    }

    GoogleSearchResponseDTO response =
        this.restClient
            .post()
            .uri("/v1/places:searchText")
            .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress")
            .body(new GoogleSearchPlacesBody(name))
            .retrieve()
            .body(GoogleSearchResponseDTO.class);

    return response;
  }

  // Negative caching methods

  @Cacheable(
      cacheNames = "placesByNameNegative",
      key =
          "'v1:' + T(com.listings_compare_api.service.GooglePlacesService).normalizeNameString(#name)")
  public boolean isNegativeCached(String name) {
    return false;
  }

  @org.springframework.cache.annotation.CachePut(
      cacheNames = "placesByNameNegative",
      key =
          "'v1:' + T(com.listings_compare_api.service.GooglePlacesService).normalizeNameString(#name)")
  public boolean cacheNegative(String name) {
    return true;
  }
}
