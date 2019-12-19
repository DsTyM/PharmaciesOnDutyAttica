import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
    This is a simple script to get info from all pharmacyIds.
 */

public class GetPharmaciesInfo {
    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        var pharmacyIds = new ArrayList<String>();

        File file = new File("pharmacyIds.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String pharmacyId;
        while ((pharmacyId = br.readLine()) != null) {
            pharmacyIds.add(pharmacyId);
        }

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=";
        String url;

        Document jsoupdoc;

        for (var pId : pharmacyIds) {
            url = tempUrl + pId + "&programmeid=1";
            jsoupdoc = Jsoup.connect(url).get();

            var listOfPharmacyInfo = new ArrayList<String[]>();

            String pageInfo;
            var tempXPath = "html body center table tbody tr:eq(1) td table tbody tr";

            var tempArr = new String[4];
            /*
                1) Pharmacy Name
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
            listOfPharmacyInfo.add(tempArr);

            for (var info : listOfPharmacyInfo) {
                System.out.println(info[0] + ", " + info[1] + ", " + info[2] + ", " + info[3]);
            }
        }
    }
}
