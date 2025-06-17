package com.example.petmanagement;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String PETS_CACHE_NAME = "pets";
    public static final String PETS_CACHE_ID_KEY = "#id";
    @Bean
    public CacheManager cachePetManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(PETS_CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(100));
        return cacheManager;
    }
}