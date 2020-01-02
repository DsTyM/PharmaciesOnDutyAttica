import entity.Pharmacy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
    This is a simple script to get info from all pharmacyIds.
 */

public class GetPharmaciesInfo {
    public static ArrayList<Pharmacy> getPharmaciesInfo() throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

//        var ids = IntStream.range(4020, 7920).toArray();
        var ids = IntStream.range(4020, 4030).toArray();

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=";
        String url;

        Document jsoupdoc;

        var listOfPharmacies = new ArrayList<Pharmacy>();

        for (var pId : ids) {
            url = tempUrl + pId + "&programmeid=1";

            try {
                jsoupdoc = Jsoup.connect(url).get();
            } catch (Exception e) {
                System.out.println("entity.Pharmacy with URL: " + url + " does not exist!");

                // if pharmacy url does not exist
                continue;
            }

            String pageInfo;
            var tempXPath = "html body center table tbody tr:eq(1) td table tbody tr";

            // temp String Array to temporarily save the pharmacy info taken from the web
            var tempArr = new String[4];
            /*
                1) entity.Pharmacy Name
                2) Address
                3) Area
                4) Phone Number
             */

            for (var i = 2; i <= 5; i++) {
                var xPath = tempXPath + ":eq(" + i + ")";
                pageInfo = jsoupdoc.select(xPath).text();
                pageInfo = pageInfo.substring(pageInfo.indexOf(":") + 1).trim();
                tempArr[i - 2] = pageInfo;
            }

            // transfer pharmacy info from tempArr to entity.Pharmacy class
            var pharmacy = new Pharmacy(Integer.parseInt(String.valueOf(pId)), tempArr[0], tempArr[1], tempArr[2], tempArr[3]);

            listOfPharmacies.add(pharmacy);
        }

        return listOfPharmacies;
    }
}
