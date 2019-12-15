import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class GetWorkingHoursInfo {
    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=4028&programmeid=";

        String url;
        Document jsoupdoc;

        // Working Hours Ids seem to be from 1 to 29 and from 32 to 40
        var workingHoursIds = new ArrayList<String>();
        for (int i = 1; i <= 40; i++) {
            int finalI = i;

            // if i not in...
            if (IntStream.of(14, 15, 16, 17, 21, 22, 30, 31).noneMatch(x -> x == finalI)) {
                workingHoursIds.add(Integer.toString(i));
            }
        }

        for (var id : workingHoursIds) {

            url = tempUrl + id;
            jsoupdoc = Jsoup.connect(url).get();

            var listOfPharmacyInfo = new ArrayList<String[]>();

            String pageInfo;
            var xPath = "html body center table tbody tr:eq(1) td table tbody tr:eq(1)";
            pageInfo = jsoupdoc.select(xPath).text();
            pageInfo = pageInfo.substring(pageInfo.indexOf(":") + 1).trim();

            var tempArr = new String[4];
        /*
            1) Working Hours Id
            2) Working Hours
         */

            tempArr[0] = id;
            tempArr[1] = pageInfo;
            listOfPharmacyInfo.add(tempArr);

            for (var info : listOfPharmacyInfo) {
                System.out.println(info[0] + ", " + info[1]);
            }
        }
    }
}
