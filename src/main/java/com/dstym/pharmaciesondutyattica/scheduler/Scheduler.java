package com.dstym.pharmaciesondutyattica.scheduler;

import com.dstym.pharmaciesondutyattica.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
    private final ScraperService scraperService;
    private final CacheManager cacheManager;

    /**
     * Clears specific caches managed by the `CacheManager`.
     * <p>
     * If any of the caches are not found, a `NullPointerException` is thrown.
     */
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
        scraperService.saveAvailablePharmacies(daysFromToday);
        clearCache();
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void getAvailablePharmaciesForAllWeek() {
        var numOfDays = 7;
        scraperService.saveAvailablePharmaciesForLastDays(numOfDays);
        clearCache();
    }

    // Run only once after startup and get the pharmacies on duty for today and the next 6 days.
    @EventListener(ApplicationReadyEvent.class)
    public void getAvailablePharmaciesAfterStartup() {
        var numOfDays = 7;
        scraperService.saveAvailablePharmaciesForLastDays(numOfDays);
        clearCache();
    }
}
