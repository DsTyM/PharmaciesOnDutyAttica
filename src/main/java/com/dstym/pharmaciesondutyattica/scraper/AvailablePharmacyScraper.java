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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    public static void saveAvailablePharmacies(int daysFromToday) {
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var lastPulledVersion = getLastPulledVersion(date);

        try {
            var page = getHTMLPageFromWebClient();
            var jsoupdoc = Jsoup.parse(page.asXml());
            var elements = jsoupdoc.select("html body div main div table tbody tr");
            for (var element : elements) {
                AvailablePharmacy availablePharmacy = new AvailablePharmacy();
                availablePharmacy.setDate(date);
                availablePharmacy.setPulledVersion(lastPulledVersion + 1);

                var region = element.select("td").get(2).text().trim();
                var name = element.select("td").get(3).text().trim();
                var address = element.select("td").get(4).text().trim();
                var phoneNumber = element.select("td").get(5).text().trim();
                var workingHourText = element.select("td").get(6).text().trim();

                var pharmacies = pharmacyRepository.findAllByName(name);

                for (var pharmacy : pharmacies) {
                    if (pharmacy.getRegion().equals(region) && pharmacy.getAddress().equals(address)
                            && pharmacy.getPhoneNumber().equals(phoneNumber)) {
                        availablePharmacy.setPharmacy(pharmacy);
                    }
                }

                if (availablePharmacy.getPharmacy() == null) {
                    Pharmacy newPharmacy = new Pharmacy();
                    newPharmacy.setName(name);
                    newPharmacy.setRegion(region);
                    newPharmacy.setAddress(address);
                    newPharmacy.setPhoneNumber(phoneNumber);
                    availablePharmacy.setPharmacy(pharmacyRepository.save(newPharmacy));
                }

                var workingHours = workingHourRepository.findFirstByWorkingHourText(workingHourText);
                if (!workingHours.isEmpty()) {
                    availablePharmacy.setWorkingHour(workingHours.get(0));
                } else {
                    WorkingHour newWorkingHour = new WorkingHour();
                    newWorkingHour.setWorkingHourText(workingHourText);
                    availablePharmacy.setWorkingHour(workingHourRepository.save(newWorkingHour));
                }

                availablePharmacyRepository.save(availablePharmacy);
            }

            log.info("Available pharmacies have been saved.");
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
        }
    }

    private static int getLastPulledVersion(String date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);
        var lastPulledVersion = 0;

        if (!result.isEmpty()) {
            var tempAvailablePharmacy = result.get(0);
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        return lastPulledVersion;
    }

    private static HtmlPage getHTMLPageFromWebClient() throws IOException {
        final var url = "http://fsa-efimeries.gr";

        var webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        return webClient.getPage(url);
    }

    @PostConstruct
    private void init() {
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
    }
}
