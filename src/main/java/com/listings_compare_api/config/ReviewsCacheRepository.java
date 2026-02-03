package com.listings_compare_api.config;

import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewsCacheRepository {

    private final StringRedisTemplate redis;
    private final CacheConfig config;

    public ReviewsCacheRepository(StringRedisTemplate redis, CacheConfig config) {
        this.redis = redis;
        this.config = config;
    }

    public Optional<String> getReviews(String placeId) {
        return Optional.ofNullable(redis.opsForValue().get(key(placeId)));
    }

    public void saveReviews(String placeId, String json) {
        redis.opsForValue().set(key(placeId), json, Duration.ofDays(config.reviewsTtlDays()));
    }

    public void saveNegative(String placeId) {
        redis.opsForValue().set(negKey(placeId), "1", Duration.ofDays(config.negativeTtlDays()));
    }

    public boolean isNegativeCached(String placeId) {
        return Boolean.TRUE.equals(redis.hasKey(negKey(placeId)));
    }

    private String key(String id) {
        return "reviews:" + id;
    }

    private String negKey(String id) {
        return "reviews:neg:" + id;
    }
}
