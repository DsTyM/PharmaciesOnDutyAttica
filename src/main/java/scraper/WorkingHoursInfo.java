package scraper;

import entity.WorkingHour;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
    This is a simple script to get all Working Hours given the workingHoursId.
 */

public class WorkingHoursInfo {
    public static ArrayList<WorkingHour> getWorkingHoursInfo() throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=4028&programmeid=";

        String url;
        Document jsoupdoc;

        // Working Hours Ids seem to be from 1 to 40
        var workingHoursIds = IntStream.range(1, 40).toArray();

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
