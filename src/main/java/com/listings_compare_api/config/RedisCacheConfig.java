package com.listings_compare_api.config;

import java.time.Duration;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisCacheConfig {

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, CacheConfig cacheConfig) {
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
    RedisSerializationContext.SerializationPair<Object> pair =
        RedisSerializationContext.SerializationPair.fromSerializer(serializer);

    RedisCacheConfiguration base =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(pair)
            .disableCachingNullValues();

    Map<String, RedisCacheConfiguration> perCache = Map.of(
        "placesByName", base.entryTtl(Duration.ofDays(cacheConfig.placeIdTtlDays())),
        "placesByNameNegative", base.entryTtl(Duration.ofDays(cacheConfig.negativeTtlDays()))
    );

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(base)
        .withInitialCacheConfigurations(perCache)
        .build();
  }
}
