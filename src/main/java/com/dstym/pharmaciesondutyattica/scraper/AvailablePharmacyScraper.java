package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.time.Instant;
import java.util.logging.Logger;

@Component
@Slf4j
public class AvailablePharmacyScraper {
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

    @PostConstruct
    private void init() {
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
    }

    public void saveAvailablePharmacies(int daysFromToday) {
        final var url = "http://fsa-efimeries.gr";
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var lastPulledVersion = getLastPulledVersion(DateUtils.stringDateToInstant(date));

        try {
            var webClient = getWebClient();
            var requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
            requestSettings.setRequestBody("Date=" + date);
            var page = (HtmlPage) webClient.getPage(requestSettings);

            var jsoupdoc = Jsoup.parse(page.asXml());
            var elements = jsoupdoc.select("html body div main div table tbody tr");
            for (var element : elements) {
                saveAvailablePharmacy(date, lastPulledVersion, element);
            }

            webClient.close();
            log.info("Available pharmacies have been updated for " + date + ".");
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            log.info("Could not update available pharmacies for " + date + ".");
        }
    }

    private void saveAvailablePharmacy(String date, int lastPulledVersion, Element element) {
        var availablePharmacy = new AvailablePharmacy();
        availablePharmacy.setDate(DateUtils.stringDateToInstant(date));
        availablePharmacy.setPulledVersion(lastPulledVersion + 1);

        var scrapedAvailablePharmacy = getScrapedAvailablePharmacy(element);

        var pharmacies = pharmacyRepository.findAllByName(scrapedAvailablePharmacy.getPharmacy().getName());

        for (var pharmacy : pharmacies) {
            if (pharmacy.getRegion().equals(scrapedAvailablePharmacy.getPharmacy().getRegion())
                    && pharmacy.getAddress().equals(scrapedAvailablePharmacy.getPharmacy().getAddress())
                    && pharmacy.getPhoneNumber().equals(scrapedAvailablePharmacy.getPharmacy().getPhoneNumber())) {
                availablePharmacy.setPharmacy(pharmacy);
            }
        }

        if (availablePharmacy.getPharmacy() == null) {
            savePharmacy(availablePharmacy, scrapedAvailablePharmacy.getPharmacy());
        }

        var workingHours = workingHourRepository.findFirstByWorkingHourText(scrapedAvailablePharmacy.getWorkingHour().getWorkingHourText());
        if (!workingHours.isEmpty()) {
            availablePharmacy.setWorkingHour(workingHours.get(0));
        } else {
            saveWorkingHour(availablePharmacy, scrapedAvailablePharmacy.getWorkingHour());
        }

        availablePharmacyRepository.save(availablePharmacy);
    }

    private AvailablePharmacy getScrapedAvailablePharmacy(Element element) {
        var availablePharmacy = new AvailablePharmacy();

        var pharmacy = new Pharmacy();
        pharmacy.setRegion(element.select("td").get(2).text().trim());
        pharmacy.setName(element.select("td").get(3).text().trim());
        pharmacy.setAddress(element.select("td").get(4).text().trim());
        pharmacy.setPhoneNumber(element.select("td").get(5).text().trim());
        availablePharmacy.setPharmacy(pharmacy);

        var workingHour = new WorkingHour();
        workingHour.setWorkingHourText(element.select("td").get(6).text().trim());
        availablePharmacy.setWorkingHour(workingHour);

        return availablePharmacy;
    }

    private void saveWorkingHour(AvailablePharmacy availablePharmacy, WorkingHour scrapedWorkingHour) {
        var newWorkingHour = new WorkingHour();
        newWorkingHour.setWorkingHourText(scrapedWorkingHour.getWorkingHourText());
        availablePharmacy.setWorkingHour(workingHourRepository.save(newWorkingHour));
    }

    private void savePharmacy(AvailablePharmacy availablePharmacy, Pharmacy scrappedPharmacy) {
        var newPharmacy = new Pharmacy();
        newPharmacy.setName(scrappedPharmacy.getName());
        newPharmacy.setRegion(scrappedPharmacy.getRegion());
        newPharmacy.setAddress(scrappedPharmacy.getAddress());
        newPharmacy.setPhoneNumber(scrappedPharmacy.getPhoneNumber());
        availablePharmacy.setPharmacy(pharmacyRepository.save(newPharmacy));
    }

    private int getLastPulledVersion(Instant date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);
        var lastPulledVersion = 0;

        if (!result.isEmpty()) {
            var tempAvailablePharmacy = result.get(0);
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        return lastPulledVersion;
    }

    private WebClient getWebClient() {
        var webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        return webClient;
    }
}
