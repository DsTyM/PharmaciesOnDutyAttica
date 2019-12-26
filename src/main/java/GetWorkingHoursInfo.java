import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
    This is a simple script to get all Working Hours given the workingHoursId.
 */

public class GetWorkingHoursInfo {
    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=4028&programmeid=";

        String url;
        Document jsoupdoc;

        // Working Hours Ids seem to be from 1 to 29 and from 32 to 40
        var workingHoursIds = new ArrayList<Integer>();
        for (int i = 1; i <= 40; i++) {
            int finalI = i;

            // if i not in...
            if (IntStream.of(14, 15, 16, 17, 21, 22, 30, 31).noneMatch(x -> x == finalI)) {
                workingHoursIds.add(i);
            }
        }

        var listOfWorkingHours = new ArrayList<WorkingHour>();

        for (var id : workingHoursIds) {
            url = tempUrl + id;
            jsoupdoc = Jsoup.connect(url).get();

            String pageInfo;
            var xPath = "html body center table tbody tr:eq(1) td table tbody tr:eq(1)";
            pageInfo = jsoupdoc.select(xPath).text();
            pageInfo = pageInfo.substring(pageInfo.indexOf(":") + 1).trim();

            var workingHour = new WorkingHour(id, pageInfo);
            listOfWorkingHours.add(workingHour);
        }

        for (var workingHour : listOfWorkingHours) {
            System.out.println(workingHour);
        }
    }
}
