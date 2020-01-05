package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

/*
    This is a simple script to get all Working Hours given the workingHoursId.
 */

@Component
public class WorkingHourScraper {
    private static WorkingHourRepository workingHourRepository;

    @Autowired
    WorkingHourScraper(WorkingHourRepository workingHourRepository) {
        WorkingHourScraper.workingHourRepository = workingHourRepository;
    }

    public static void getWorkingHours() {
        var listOfWorkingHours = WorkingHourScraper.getWorkingHoursInfo();
        for (var workingHour : listOfWorkingHours) {
            // if id found, update
            // else add
            workingHourRepository.save(workingHour);
        }

        System.out.println("Operation Completed!");
    }

    private static ArrayList<WorkingHour> getWorkingHoursInfo() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=4028&programmeid=";

        String url;
        Document jsoupdoc;

        // Working Hours Ids seem to be from 1 to 41
        var workingHoursIds = IntStream.range(1, 41).toArray();

        var listOfWorkingHours = new ArrayList<WorkingHour>();

        for (var id : workingHoursIds) {
            url = tempUrl + id;

            try {
                jsoupdoc = Jsoup.connect(url).get();
            } catch (Exception e) {
                continue;
            }

            String pageInfo;
            var xPath = "html body center table tbody tr:eq(1) td table tbody tr:eq(1)";
            pageInfo = jsoupdoc.select(xPath).text();
            pageInfo = pageInfo.substring(pageInfo.indexOf(":") + 1).trim();

            var workingHour = new WorkingHour(id, pageInfo);
            listOfWorkingHours.add(workingHour);
        }

        return listOfWorkingHours;
    }
}
