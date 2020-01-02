package scraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    This code is just for playing with HtmlUnit and Jsoup.
    Later it will be refactored into Classes, Methods, Best Practices, etc.
 */

public class AvailablePharmacies {
    public static HashMap<Integer, Integer> getAvailablePharmacyIdsAndWorkingHourIds(int daysFromToday) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

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

        int getPositionOfSecondEqualsChar;
        int getPositionOfAndSymbolChar;
        int getPositionOfLastEqualsChar;
        int getPositionOfLastApostropheChar;
        String pharmacyId;
        String workingHourId;

        // HashMap<PharmacyId, WorkingHoursId>
        var workingHoursIdByPharmacyId = new HashMap<Integer, Integer>();

        try {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            // Load the page

            page = webClient.getPage(url);

            // Get ids from all dates

            var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));

            System.out.println(date);
            select = page.getForms().get(0).getSelectByName("dateduty");
            option = select.getOptionByValue(date);
            select.setSelectedAttribute(option, true);

            if (daysFromToday < -1) {
                return null;
            } else if (daysFromToday == 0) {
                input = page.getForms().get(0).getInputsByValue("").get(2);
            } else {
                input = page.getForms().get(0).getInputsByValue("").get(1);
            }

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

            for (var singlePage : pages) {
                jsoupdoc = Jsoup.parse(singlePage.asXml());
                pharmacyLinksJs = jsoupdoc.select("html body table tbody tr td:eq(1) table tbody tr:eq(3) td table tbody tr a").eachAttr("onclick");

                for (String linkJs : pharmacyLinksJs) {
                    linkJs = linkJs.trim();
                    getPositionOfSecondEqualsChar = linkJs.indexOf("=", linkJs.indexOf("=") + 1);
                    getPositionOfAndSymbolChar = linkJs.indexOf("&", getPositionOfSecondEqualsChar);
                    pharmacyId = linkJs.substring(getPositionOfSecondEqualsChar + 1, getPositionOfAndSymbolChar);

                    getPositionOfLastEqualsChar = linkJs.lastIndexOf("=");
                    getPositionOfLastApostropheChar = linkJs.lastIndexOf("'");
                    workingHourId = linkJs.substring(getPositionOfLastEqualsChar + 1, getPositionOfLastApostropheChar);

                    workingHoursIdByPharmacyId.put(Integer.parseInt(pharmacyId), Integer.parseInt(workingHourId));
                }
            }

            return workingHoursIdByPharmacyId;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
