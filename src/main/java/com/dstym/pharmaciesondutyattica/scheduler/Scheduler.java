package com.dstym.pharmaciesondutyattica.scheduler;

import com.dstym.pharmaciesondutyattica.scraper.AvailablePharmacyScraper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class Scheduler {
    // cron properties
    // <second> <minute> <hour> <day-of-month> <month> <day-of-week> <year>

    @Scheduled(cron = "0 5/12 * * * *")
    public void getAvailablePharmaciesTwicePerDay() {
        var daysFromToday = 0;
        AvailablePharmacyScraper.getAvailablePharmacies(daysFromToday);
    }

    // run only once after startup
//    @EventListener(ApplicationReadyEvent.class)
//    public void getAvailablePharmaciesAfterStartup() {
//        var daysFromToday = 0;
//        AvailablePharmacyScraper.getAvailablePharmacies(daysFromToday);
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void getAvailablePharmaciesForLastDaysAfterStartup() {
//        var numOfDays = 5;
//        AvailablePharmacyScraper.getAvailablePharmaciesForLastDays(numOfDays);
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void getPharmaciesAndWorkingHoursAfterStartup() {
//        PharmacyScraper.getPharmacies();
//        WorkingHourScraper.getWorkingHours();
//    }
}
