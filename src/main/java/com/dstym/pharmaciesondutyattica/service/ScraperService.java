package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.HttpMethod;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperService {
    private final AvailablePharmacyRepository availablePharmacyRepository;
    private final PharmacyRepository pharmacyRepository;
    private final WorkingHourRepository workingHourRepository;

    public void saveAvailablePharmaciesForLastDays(Integer numOfDays) {
        for (var i = 0; i < numOfDays; i++) {
            saveAvailablePharmacies(i);
        }
    }

    public void saveAvailablePharmacies(Integer daysFromToday) {
        WebClient webClient = null;
        final var url = "http://fsa-efimeries.gr";
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var lastPulledVersion = getLastPulledVersion(DateUtils.stringDateToInstant(date));

        try {
            webClient = getWebClient();
            var requestSettings = new WebRequest(URI.create(url).toURL(), HttpMethod.POST);
            requestSettings.setRequestBody("Date=" + date);
            var page = (HtmlPage) webClient.getPage(requestSettings);

            var jsoupdoc = Jsoup.parse(page.asXml());
            var elements = jsoupdoc.select("html body div main div table tbody tr");
            for (var element : elements) {
                saveAvailablePharmacy(date, lastPulledVersion, element);
            }

            log.info("Available pharmacies have been updated for " + date + ".");
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            log.info("Could not update available pharmacies for " + date + ".");
        } finally {
            Optional.ofNullable(webClient).ifPresent(WebClient::close);
        }
    }

    private void saveAvailablePharmacy(String date, Integer lastPulledVersion, Element element) {
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
            availablePharmacy.setWorkingHour(workingHours.getFirst());
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
            var tempAvailablePharmacy = result.getFirst();
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        return lastPulledVersion;
    }

    private WebClient getWebClient() {
        var webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        return webClient;
    }
}
