package com.dstym.pharmaciesondutyattica.config;

import java.time.Duration;
import java.util.ArrayList;
import javax.cache.CacheManager;
import javax.cache.Caching;

import com.dstym.pharmaciesondutyattica.dto.PharmacyDto;
import com.dstym.pharmaciesondutyattica.dto.WorkingHourDto;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@EnableCaching
@Configuration
public class EhcacheConfiguration {
  @Bean
  public CacheManager EhcacheManager() {
    var cachingProvider = Caching.getCachingProvider();
    var cacheManager = cachingProvider.getCacheManager();

    cacheManager.createCache("workingHourCache",
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Integer.class,
                    WorkingHourDto.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(100, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(23)))
                .build()
        )
    );

    cacheManager.createCache("workingHoursCache",
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(PageRequest.class,
                    PageImpl.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(100, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(23)))
                .build()
        )
    );

    cacheManager.createCache("pharmacyCache",
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Integer.class,
                    PharmacyDto.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(100, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(23)))
                .build()
        )
    );

    cacheManager.createCache("pharmaciesCache",
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(ArrayList.class,
                    PageImpl.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(500, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(23)))
                .build()
        )
    );

    cacheManager.createCache("availablePharmaciesCache",
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(ArrayList.class,
                    PageImpl.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(100, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(23)))
                .build()
        )
    );

    return cacheManager;
  }
}
