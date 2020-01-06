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

                System.out.println(availablePharmacy);
                availablePharmacyRepository.save(availablePharmacy);
            }
        }
        System.out.println("Operation Completed!");
    }

    private static HashMap<Integer, Integer> getAvailablePharmacies(int daysFromToday) {
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
        int pharmacyId;
        int workingHourId;

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

            return getPharmacyIdWorkingHourIdPairFromHTMLDOM(pages);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HashMap<Integer, Integer> getPharmacyIdWorkingHourIdPairFromHTMLDOM(List<HtmlPage> pages) {
        Document jsoupdoc;

        List<String> pharmacyLinksJs;
        int pharmacyId;
        int workingHourId;

        // HashMap<PharmacyId, WorkingHoursId>
        var workingHoursIdByPharmacyId = new HashMap<Integer, Integer>();

        for (var singlePage : pages) {
            jsoupdoc = Jsoup.parse(singlePage.asXml());
            pharmacyLinksJs = jsoupdoc
                    .select("html body table tbody tr td:eq(1) table tbody tr:eq(3) td table tbody tr a")
                    .eachAttr("onclick");

            for (String linkJs : pharmacyLinksJs) {
                pharmacyId = getSinglePharmacyIdFromURL(linkJs);
                workingHourId = getSingleWorkingHourIdFromURL(linkJs);

                workingHoursIdByPharmacyId.put(pharmacyId, workingHourId);
            }
        }
        return workingHoursIdByPharmacyId;
    }

    private static int getSinglePharmacyIdFromURL(String linkJs) {
        int getPositionOfSecondEqualsChar;
        int getPositionOfAndSymbolChar;
        String stringPharmacyId;

        linkJs = linkJs.trim();
        getPositionOfSecondEqualsChar = linkJs.indexOf("=", linkJs.indexOf("=") + 1);
        getPositionOfAndSymbolChar = linkJs.indexOf("&", getPositionOfSecondEqualsChar);
        stringPharmacyId = linkJs.substring(getPositionOfSecondEqualsChar + 1, getPositionOfAndSymbolChar);

        return Integer.parseInt(stringPharmacyId);
    }

    private static int getSingleWorkingHourIdFromURL(String linkJs) {
        int getPositionOfLastEqualsChar;
        int getPositionOfLastApostropheChar;
        String stringWorkingHourId;

        getPositionOfLastEqualsChar = linkJs.lastIndexOf("=");
        getPositionOfLastApostropheChar = linkJs.lastIndexOf("'");
        stringWorkingHourId = linkJs.substring(getPositionOfLastEqualsChar + 1, getPositionOfLastApostropheChar);

        return Integer.parseInt(stringWorkingHourId);
    }
}
