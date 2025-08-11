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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

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
        final var url = "https://fsa-efimeries.gr/Home/FilteredHomeResults";
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var lastPulledVersion = getLastPulledVersion(DateUtils.stringDateToInstant(date));

        try {
            webClient = getWebClient();
            var requestSettings = new WebRequest(URI.create(url).toURL(), HttpMethod.POST);
            requestSettings.setRequestBody("IsOpen=false&Date=" + date);

            var page = (HtmlPage) webClient.getPage(requestSettings);

            var xmlPage = page.asXml();
            var jsoupdoc = Jsoup.parse(xmlPage);
            var elements = jsoupdoc.select("html > body > div:nth-of-type(2) > div > div");
            if (!isEmpty(elements)) {
                var scrapedAvailablePharmacies = elements.stream().map(this::getScrapedAvailablePharmacy).toList();
                saveAvailablePharmacies(date, lastPulledVersion, scrapedAvailablePharmacies);

                log.info("Available pharmacies have been updated for {}.", date);
            } else {
                log.error("Could not find available pharmacies for {}.", date);
            }
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            log.error("Could not update available pharmacies for {}.", date);
        } finally {
            Optional.ofNullable(webClient).ifPresent(WebClient::close);
        }
    }

    private void saveAvailablePharmacies(String date, Integer lastPulledVersion, List<AvailablePharmacy> availablePharmacies) {
        var pharmacyNames = availablePharmacies.stream().map(AvailablePharmacy::getPharmacy).map(Pharmacy::getName).toList();
        var pharmacies = pharmacyRepository.findAllByNameIn(pharmacyNames);
        var pharmaciesMap = pharmacies.stream()
                .collect(Collectors.toMap(this::getPharmacyText, pharmacy -> pharmacy, (existing, replacement) -> existing));

        var workingHoursTexts = availablePharmacies.stream().map(AvailablePharmacy::getWorkingHour).map(WorkingHour::getWorkingHourText).toList();
        var workingHours = workingHourRepository.findAllByWorkingHourTextIn(workingHoursTexts);
        var workingHoursMap = workingHours.stream().collect(Collectors.toMap(WorkingHour::getWorkingHourText, workingHour -> workingHour));

        List<Pharmacy> newPharmacies = new ArrayList<>();
        List<WorkingHour> newWorkingHours = new ArrayList<>();

        for (var availablePharmacy : availablePharmacies) {
            availablePharmacy.setDate(DateUtils.stringDateToInstant(date));
            availablePharmacy.setPulledVersion(lastPulledVersion + 1);

            if (pharmaciesMap.containsKey(getPharmacyText(availablePharmacy.getPharmacy()))) {
                var candidatePharmacy = pharmaciesMap.get(getPharmacyText(availablePharmacy.getPharmacy()));
                availablePharmacy.setPharmacy(candidatePharmacy);
            } else {
                newPharmacies.add(availablePharmacy.getPharmacy());
                pharmaciesMap.put(getPharmacyText(availablePharmacy.getPharmacy()), availablePharmacy.getPharmacy());
            }

            if (workingHoursMap.containsKey(availablePharmacy.getWorkingHour().getWorkingHourText())) {
                var candidateWorkingHour = workingHoursMap.get(availablePharmacy.getWorkingHour().getWorkingHourText());
                availablePharmacy.setWorkingHour(candidateWorkingHour);
            } else {
                newWorkingHours.add(availablePharmacy.getWorkingHour());
                workingHoursMap.put(availablePharmacy.getWorkingHour().getWorkingHourText(), availablePharmacy.getWorkingHour());
            }
        }

        pharmacyRepository.saveAll(newPharmacies);
        workingHourRepository.saveAll(newWorkingHours);

        availablePharmacyRepository.saveAll(availablePharmacies);
    }

    private AvailablePharmacy getScrapedAvailablePharmacy(Element element) {
        var availablePharmacy = new AvailablePharmacy();

        var pharmacy = new Pharmacy();
        pharmacy.setRegion(element.select("div > div:nth-of-type(1) > div:nth-of-type(2)").getFirst().text().trim());
        pharmacy.setName(element.select("div > div:nth-of-type(1) > div:nth-of-type(3) > div > span:nth-of-type(1)").getFirst().text().trim());
        pharmacy.setAddress(element.select("div > div:nth-of-type(1) > div:nth-of-type(1) > h6").getFirst().text().trim());
        pharmacy.setPhoneNumber(element.select("div > div:nth-of-type(1) > div:nth-of-type(3) > div > h6").getFirst().text().trim());
        availablePharmacy.setPharmacy(pharmacy);

        var workingHour = new WorkingHour();
        workingHour.setWorkingHourText(element.select("div > div:nth-of-type(1) > div:nth-of-type(3) > div > span:nth-of-type(2)").getFirst().text().trim());
        availablePharmacy.setWorkingHour(workingHour);

        return availablePharmacy;
    }

    private String getPharmacyText(Pharmacy pharmacy) {
        return pharmacy.getName() + " " + pharmacy.getRegion() + " " + pharmacy.getAddress() + " " + pharmacy.getPhoneNumber();
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
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        return webClient;
    }
}
