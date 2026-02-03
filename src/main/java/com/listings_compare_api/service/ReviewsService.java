package com.listings_compare_api.service;

import com.listings_compare_api.config.GooglePlacesConfig;
import com.listings_compare_api.config.ReviewsCacheRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReviewsService {

  private final ReviewsCacheRepository cache;
  private final RestClient client;
  private final int concurrency;

  public ReviewsService(
      ReviewsCacheRepository cache,
      @Qualifier("googlePlacesRestClient") RestClient client,
      GooglePlacesConfig config) {
    this.cache = cache;
    this.client = client;
    this.concurrency = config.batch().concurrency();
  }

  public String fetchRawReviewsJson(String placeId) {
    return client
        .get()
        .uri("/v1/places/{id}", placeId)
        .header("X-Goog-FieldMask", "id,rating,userRatingCount,reviews")
        .retrieve()
        .body(String.class);
  }

  public Map<String, String> batchFetch(List<String> placeIds) {

    var executor = Executors.newVirtualThreadPerTaskExecutor();
    var semaphore = new Semaphore(concurrency);

    Map<String, CompletableFuture<Optional<String>>> futures =
        placeIds.stream()
            .distinct()
            .collect(
                Collectors.toMap(
                    id -> id,
                    id ->
                        CompletableFuture.supplyAsync(
                            () -> {
                              var cached = cache.getReviews(id);
                              if (cached.isPresent()) return cached;

                              if (cache.isNegativeCached(id)) return Optional.empty();

                              try {
                                semaphore.acquire();
                                String json = this.fetchRawReviewsJson(id);
                                cache.saveReviews(id, json);
                                return Optional.of(json);
                              } catch (Exception e) {
                                cache.saveNegative(id);
                                return Optional.empty();
                              } finally {
                                semaphore.release();
                              }
                            },
                            executor)));

    return futures.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join().orElse(null)));
  }
}
