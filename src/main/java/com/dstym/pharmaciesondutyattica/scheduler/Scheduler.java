package com.dstym.pharmaciesondutyattica.scheduler;

import com.dstym.pharmaciesondutyattica.scraper.AvailablePharmacyScraper;
import org.springframework.beans.factory.annotation.Autowired;
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
    // cron properties
    // <minute> <hour> <day-of-month> <month> <day-of-week> <year>

    private final CacheManager cacheManager;

    public Scheduler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // 5 minutes, after refreshing the data
    @Scheduled(cron = "7 0/8 * * * *")
    public void reportCurrentTime() {
        Objects.requireNonNull(cacheManager.getCache("workingHourCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("workingHoursCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("pharmacyCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("pharmaciesCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("pharmaciesByRegionCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("availablePharmaciesCache")).clear();
    }

    @Scheduled(cron = "2 0/8 * * * *")
    public void getAvailablePharmaciesTwicePerDay() {
        var daysFromToday = 0;
        AvailablePharmacyScraper.saveAvailablePharmacies(daysFromToday);
    }

    // run only once after startup
    @EventListener(ApplicationReadyEvent.class)
    public void getAvailablePharmaciesAfterStartup() {
        var daysFromToday = 0;
        AvailablePharmacyScraper.saveAvailablePharmacies(daysFromToday);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void getAvailablePharmaciesForLastDaysAfterStartup() {
//        var numOfDays = 10;
//        AvailablePharmacyScraper.saveAvailablePharmaciesForLastDays(numOfDays);
//    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void getPharmaciesAndWorkingHoursAfterStartup() {
//        PharmacyScraper.savePharmacies();
//        WorkingHourScraper.saveWorkingHours();
//    }
}
