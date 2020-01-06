package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
public class WorkingHourScraper {
    private static WorkingHourRepository workingHourRepository;

    @Autowired
    WorkingHourScraper(WorkingHourRepository workingHourRepository) {
        WorkingHourScraper.workingHourRepository = workingHourRepository;
    }

    public static void saveWorkingHours() {
        var listOfWorkingHours = WorkingHourScraper.getWorkingHours();
        for (var workingHour : listOfWorkingHours) {
            // add or update
            workingHourRepository.save(workingHour);
        }

        System.out.println("All working hours have been updated!");
    }

    private static ArrayList<WorkingHour> getWorkingHours() {
        // Working Hour Ids seem to be from 1 to 41
        var workingHoursIds = IntStream.range(1, 41).toArray();

        var listOfWorkingHours = new ArrayList<WorkingHour>();

        WorkingHour workingHour;

        for (var id : workingHoursIds) {
            workingHour = getSingleWorkingHour(id);

            if (workingHour == null) {
                continue;
            }

            listOfWorkingHours.add(workingHour);
        }

        return listOfWorkingHours;
    }

    private static WorkingHour getSingleWorkingHour(int id) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=4028&programmeid=";

        String url;
        Document jsoupdoc;

        url = tempUrl + id;

        try {
            jsoupdoc = Jsoup.connect(url).get();
        } catch (Exception e) {
            return null;
        }

        var pageInfo = getWorkingHourIdFromHTMLDOM(jsoupdoc);

        return new WorkingHour(id, pageInfo);
    }

    private static String getWorkingHourIdFromHTMLDOM(Document jsoupdoc) {
        var xPath = "html body center table tbody tr:eq(1) td table tbody tr:eq(1)";
        var pageInfo = jsoupdoc.select(xPath).text();
        return pageInfo.substring(pageInfo.indexOf(":") + 1).trim();
    }
}
