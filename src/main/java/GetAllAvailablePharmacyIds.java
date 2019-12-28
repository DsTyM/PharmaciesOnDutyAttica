import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/*
    Script to get a set of all the available Pharmacy Ids.
    And save them to file: pharmacyIds.txt .
 */

public class GetAllAvailablePharmacyIds {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        var setOfIds = new HashSet<String>();

        final var url = "http://www.fsa.gr/duties.asp";

        WebClient webClient;
        HtmlPage page;
        HtmlInput input;
        List<HtmlAnchor> anchors;
        List<HtmlPage> pages = new ArrayList<>();

        Document jsoupdoc;
        int numOfPages;

        HtmlSelect select;
        HtmlOption option;

        List<String> pharmacyLinksJs;
        String tempLinkJs;

        int getPositionOfSecondEqualsChar;
        int getPositionOfAndSymbolChar;
        String pharmacyId;

        try {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            // Load the page

            page = webClient.getPage(url);

            // Get ids from all dates

            // It should get about last ten days.
            var stringDates = new ArrayList<String>();
            for (var i = 28; i <= 31; i++) {
                stringDates.add(i + "/12/2019");
            }
            for (var i = 1; i <= 5; i++) {
                stringDates.add(i + "/1/2020");
            }

            System.out.println(Arrays.toString(stringDates.toArray()));

            for (var date : stringDates) {
                System.out.println(date);
                select = page.getForms().get(0).getSelectByName("dateduty");
                option = select.getOptionByValue(date);
                select.setSelectedAttribute(option, true);

                input = page.getForms().get(0).getInputsByValue("").get(1);

                // Click Search

                page = input.click();

                // jsoup Code

                jsoupdoc = Jsoup.parse(page.asXml());
                var numOfPagesAsText = jsoupdoc.select("html body table tbody tr td:eq(1) table tbody tr:eq(4) td table tbody tr td nobr").text().trim();
                // this equals this XPath: /html/body/table/tbody/tr/td[2]/table/tbody/tr[5]/td/table/tbody/tr[1]/td/nobr


                // If there are more than one pages.
                if (!numOfPagesAsText.equals("")) {
                    numOfPages = Integer.parseInt(numOfPagesAsText.substring(numOfPagesAsText.lastIndexOf(" ") + 1));
                } else {
                    numOfPages = 1;
                }

                pages.add(page);

                // Click next until the last page.

                anchors = page.getAnchors();

                for (var i = 0; i < numOfPages - 1; i++) {
                    page = anchors.get(10).click();
                    pages.add(page);
                }
            }

            for (var singlePage : pages) {
                jsoupdoc = Jsoup.parse(singlePage.asXml());
                pharmacyLinksJs = jsoupdoc.select("html body table tbody tr td:eq(1) table tbody tr:eq(3) td table tbody tr a").eachAttr("onclick");

                // an error seems to occur at next or previous line
                tempLinkJs = pharmacyLinksJs.toArray()[0].toString().trim();

                getPositionOfSecondEqualsChar = tempLinkJs.indexOf("=", tempLinkJs.indexOf("=") + 1);
                getPositionOfAndSymbolChar = tempLinkJs.indexOf("&", getPositionOfSecondEqualsChar);
                pharmacyId = tempLinkJs.substring(getPositionOfSecondEqualsChar + 1, getPositionOfAndSymbolChar);
                setOfIds.add(pharmacyId);
            }

            Files.deleteIfExists(Paths.get("pharmacyIds.txt"));
            try (FileWriter fw = new FileWriter("pharmacyIds.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                for (var id : setOfIds) {
                    out.println(id);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getLastStringDates(int numOfDays) {
        return null;
    }
}
