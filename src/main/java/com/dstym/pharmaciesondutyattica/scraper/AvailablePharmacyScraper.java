package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AvailablePharmacyScraper {
    private static AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    public AvailablePharmacyScraper(AvailablePharmacyRepository availablePharmacyRepository) {
        AvailablePharmacyScraper.availablePharmacyRepository = availablePharmacyRepository;
    }

    public static void saveAvailablePharmaciesForLastDays(int numOfDays) {
        for (var i = 0; i < numOfDays; i++) {
            saveAvailablePharmacies(i);
        }
    }

    public static void saveAvailablePharmacies(int daysFromToday) {
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var workingHoursIdByPharmacyId = getAvailablePharmacies(daysFromToday);
        AvailablePharmacy availablePharmacy;

        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        int lastPulledVersion = 0;

        if (!result.isEmpty()) {
            var tempAvailablePharmacy = (AvailablePharmacy) result.toArray()[0];
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        if (workingHoursIdByPharmacyId != null) {
            for (var pair : workingHoursIdByPharmacyId.keySet()) {
                int pharmacyId = pair;
                int workingHourId = workingHoursIdByPharmacyId.get(pair);

                var tempPharmacy = new Pharmacy();
                tempPharmacy.setId(pharmacyId);

                var tempWorkingHour = new WorkingHour();
                tempWorkingHour.setId(workingHourId);

                availablePharmacy = new AvailablePharmacy();
                availablePharmacy.setId(0);
                availablePharmacy.setPharmacy(tempPharmacy);
                availablePharmacy.setWorkingHour(tempWorkingHour);
                availablePharmacy.setDate(date);
                availablePharmacy.setPulledVersion(lastPulledVersion + 1);

//                System.out.println(availablePharmacy);
                availablePharmacyRepository.save(availablePharmacy);
            }
        }
        System.out.println("Available pharmacies have been updated!");
    }

    private static HashMap<Integer, Integer> getAvailablePharmacies(int daysFromToday) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        HtmlPage page;
        List<HtmlAnchor> anchors;
        List<HtmlPage> pages = new ArrayList<>();

        int numOfPages;

        try {
            var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));

            page = getHTMLPageFromWebClient();

            selectDateFromHTMLPage(page, date);

            if (daysFromToday < -1) {
                return null;
            }

            page = clickToSearchForAvailablePharmacies(page, daysFromToday);

            // saves the first page before it'll navigate to the next pages
            pages.add(page);

            numOfPages = getNumOfPagesWithPharmacies(page);

            // Click next until the last page.

            anchors = page.getAnchors();

            for (var i = 0; i < numOfPages - 1; i++) {
                page = anchors.get(10).click();
                pages.add(page);
            }

            return getPharmacyIdWorkingHourIdPairFromHTMLDOM(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getNumOfPagesWithPharmacies(HtmlPage page) {
        int numOfPages;

        var jsoupdoc = Jsoup.parse(page.asXml());
        var numOfPagesAsText = jsoupdoc
                .select("html body table tbody tr td:eq(1) table tbody tr:eq(4) td table tbody tr td nobr")
                .text().trim();
        // this equals this XPath: /html/body/table/tbody/tr/td[2]/table/tbody/tr[5]/td/table/tbody/tr[1]/td/nobr

        // If there are more than one pages.
        if (!numOfPagesAsText.equals("")) {
            numOfPages = Integer.parseInt(numOfPagesAsText.substring(numOfPagesAsText.lastIndexOf(" ") + 1));
        } else {
            numOfPages = 1;
        }

        return numOfPages;
    }

    private static HtmlPage clickToSearchForAvailablePharmacies(HtmlPage page, int daysFromToday) throws IOException {
        HtmlInput input;

        if (daysFromToday == 0) {
            try {
                input = page.getForms().get(0).getInputsByValue("").get(2);
            } catch (Exception e) {
                input = page.getForms().get(0).getInputsByValue("").get(1);
            }
        } else {
            input = page.getForms().get(0).getInputsByValue("").get(1);
        }

        return input.click();
    }

    private static void selectDateFromHTMLPage(HtmlPage page, String date) {
        var select = page.getForms().get(0).getSelectByName("dateduty");
        var option = select.getOptionByValue(date);
        select.setSelectedAttribute(option, true);
    }

    private static HtmlPage getHTMLPageFromWebClient() throws IOException {
        final var url = "http://www.fsa.gr/duties.asp";

        var webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        return webClient.getPage(url);
    }

    private static HashMap<Integer, Integer> getPharmacyIdWorkingHourIdPairFromHTMLDOM(List<HtmlPage> pages) {
        // HashMap<PharmacyId, WorkingHoursId>
        var workingHoursIdByPharmacyId = new HashMap<Integer, Integer>();

        for (var singlePage : pages) {
            var jsoupdoc = Jsoup.parse(singlePage.asXml());
            var pharmacyLinksJs = jsoupdoc
                    .select("html body table tbody tr td:eq(1) table tbody tr:eq(3) td table tbody tr a")
                    .eachAttr("onclick");

            for (String linkJs : pharmacyLinksJs) {
                var pharmacyId = getSinglePharmacyIdFromURL(linkJs);
                var workingHourId = getSingleWorkingHourIdFromURL(linkJs);

                workingHoursIdByPharmacyId.put(pharmacyId, workingHourId);
            }
        }
        return workingHoursIdByPharmacyId;
    }

    private static int getSinglePharmacyIdFromURL(String linkJs) {
        linkJs = linkJs.trim();
        var getPositionOfSecondEqualsChar = linkJs.indexOf("=", linkJs.indexOf("=") + 1);
        var getPositionOfAndSymbolChar = linkJs.indexOf("&", getPositionOfSecondEqualsChar);
        var stringPharmacyId = linkJs.substring(getPositionOfSecondEqualsChar + 1, getPositionOfAndSymbolChar);

        return Integer.parseInt(stringPharmacyId);
    }

    private static int getSingleWorkingHourIdFromURL(String linkJs) {
        var getPositionOfLastEqualsChar = linkJs.lastIndexOf("=");
        var getPositionOfLastApostropheChar = linkJs.lastIndexOf("'");
        var stringWorkingHourId = linkJs.substring(getPositionOfLastEqualsChar + 1, getPositionOfLastApostropheChar);

        return Integer.parseInt(stringWorkingHourId);
    }
}
