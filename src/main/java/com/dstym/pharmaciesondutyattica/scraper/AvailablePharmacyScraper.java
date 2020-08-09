package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AvailablePharmacyScraper {
    private static final Logger logger = LoggerFactory.getLogger(AvailablePharmacyScraper.class);
    private static AvailablePharmacyRepository availablePharmacyRepository;
    private static PharmacyRepository pharmacyRepository;
    private static WorkingHourRepository workingHourRepository;

    @Autowired
    public AvailablePharmacyScraper(AvailablePharmacyRepository availablePharmacyRepository,
                                    PharmacyRepository pharmacyRepository,
                                    WorkingHourRepository workingHourRepository) {
        AvailablePharmacyScraper.availablePharmacyRepository = availablePharmacyRepository;
        AvailablePharmacyScraper.pharmacyRepository = pharmacyRepository;
        AvailablePharmacyScraper.workingHourRepository = workingHourRepository;
    }

    public static void saveAvailablePharmaciesForLastDays(int numOfDays) {
        for (var i = 0; i < numOfDays; i++) {
            saveAvailablePharmacies(i);
        }
    }

    public static void saveAvailablePharmacies(int daysFromToday) {
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var pharmacyIdWorkingHourIdPair = getAvailablePharmacies(daysFromToday);

        var lastPulledVersion = getLastPulledVersion(date);

        if (pharmacyIdWorkingHourIdPair != null) {
            for (var pharmacyId : pharmacyIdWorkingHourIdPair.keySet()) {
                var workingHourId = pharmacyIdWorkingHourIdPair.get(pharmacyId);

                var tempPharmacy = new Pharmacy();
                tempPharmacy.setId(pharmacyId);

                var tempWorkingHour = new WorkingHour();
                tempWorkingHour.setId(workingHourId);

                var availablePharmacy = new AvailablePharmacy(0, tempPharmacy, tempWorkingHour, date,
                        lastPulledVersion + 1);

                saveAvailablePharmacy(availablePharmacy);
            }

            logger.info("Available pharmacies have been updated for " + date + ".");
        }
    }

    private static void saveAvailablePharmacy(AvailablePharmacy availablePharmacy) {
        try {
            availablePharmacyRepository.save(availablePharmacy);
        } catch (Exception e) {
            var tempPharmacy = PharmacyScraper.getSinglePharmacy(availablePharmacy.getPharmacy().getId());
            if (tempPharmacy != null) {
                pharmacyRepository.save(tempPharmacy);
            }

            var tempWorkingHour = WorkingHourScraper.getSingleWorkingHour(availablePharmacy.getWorkingHour().getId());
            if (tempWorkingHour != null) {
                workingHourRepository.save(tempWorkingHour);
            }

            availablePharmacyRepository.save(availablePharmacy);
        }
    }

    private static HashMap<Integer, Integer> getAvailablePharmacies(int daysFromToday) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        if (daysFromToday < -1) {
            return null;
        }

        try {
            var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));

            var page = getHTMLPageFromWebClient();

            selectDateFromHTMLPage(page, date);

            page = clickToSearchForAvailablePharmacies(page, daysFromToday);

            var pages = getAllPages(page);

            return getPharmacyIdWorkingHourIdPairFromHTMLDOM(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<HtmlPage> getAllPages(HtmlPage page) throws IOException {
        var pages = new ArrayList<HtmlPage>();

        // saves the first page before it'll navigate to the next pages
        pages.add(page);

        var numOfPages = getNumOfPagesWithPharmacies(page);

        var clickForNextPageURL = page.getAnchors().get(10);

        // Click next until the last page.
        for (var i = 0; i < numOfPages - 1; i++) {
            page = clickForNextPageURL.click();
            pages.add(page);
        }

        return pages;
    }

    private static int getLastPulledVersion(String date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);
        var lastPulledVersion = 0;

        if (!result.isEmpty()) {
            var tempAvailablePharmacy = (AvailablePharmacy) result.toArray()[0];
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        return lastPulledVersion;
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
