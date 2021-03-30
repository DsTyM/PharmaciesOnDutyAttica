package com.dstym.pharmaciesondutyattica.scheduler;

import com.dstym.pharmaciesondutyattica.scraper.AvailablePharmacyScraper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@EnableScheduling
@Component
public class Scheduler {
    private final AvailablePharmacyScraper availablePharmacyScraper;
    private final CacheManager cacheManager;

    public Scheduler(AvailablePharmacyScraper availablePharmacyScraper,
                     CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.availablePharmacyScraper = availablePharmacyScraper;
    }

    public void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("workingHourCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("workingHoursCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("pharmacyCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("pharmaciesCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("availablePharmaciesCache")).clear();
    }

    @Scheduled(cron = "0 0 0/6 * * *")
    public void getAvailablePharmaciesToday() {
        var daysFromToday = 0;
        availablePharmacyScraper.saveAvailablePharmacies(daysFromToday);
        clearCache();
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void getAvailablePharmaciesForAllWeek() {
        var numOfDays = 7;
        availablePharmacyScraper.saveAvailablePharmaciesForLastDays(numOfDays);
        clearCache();
    }

    // Run only once after startup and get the pharmacies on duty for the today and the next 6 days.
    @EventListener(ApplicationReadyEvent.class)
    public void getAvailablePharmaciesAfterStartup() {
        var numOfDays = 7;
        availablePharmacyScraper.saveAvailablePharmaciesForLastDays(numOfDays);
        clearCache();
    }
}
