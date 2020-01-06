package com.dstym.pharmaciesondutyattica.scheduler;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class Scheduler {
    // cron properties
    // <minute> <hour> <day-of-month> <month> <day-of-week> <year>

//    @Scheduled(cron = "0 5/12 * * * *")
//    public void getAvailablePharmaciesTwicePerDay() {
//        var daysFromToday = 0;
//        AvailablePharmacyScraper.getAvailablePharmacies(daysFromToday);
//    }

    // run only once after startup
//    @EventListener(ApplicationReadyEvent.class)
//    public void getAvailablePharmaciesAfterStartup() {
//        var daysFromToday = 0;
//        AvailablePharmacyScraper.getAvailablePharmacies(daysFromToday);
//    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void getAvailablePharmaciesForLastDaysAfterStartup() {
//        var numOfDays = 5;
//        AvailablePharmacyScraper.getAvailablePharmaciesForLastDays(numOfDays);
//    }

    @EventListener(ApplicationReadyEvent.class)
    public void getPharmaciesAndWorkingHoursAfterStartup() {
//        PharmacyScraper.getPharmacies();
//        WorkingHourScraper.saveWorkingHours();
    }
}
