package com.dstym.pharmaciesondutyattica.scraper;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
public class PharmacyScraper {
    private static final Logger logger = LoggerFactory.getLogger(AvailablePharmacyScraper.class);
    private static PharmacyRepository pharmacyRepository;

    @Autowired
    PharmacyScraper(PharmacyRepository pharmacyRepository) {
        PharmacyScraper.pharmacyRepository = pharmacyRepository;
    }

    public static void savePharmacies() {
        var listOfPharmacies = getPharmacies();
        for (var pharmacy : listOfPharmacies) {
            // add or update
            pharmacyRepository.save(pharmacy);
        }

        logger.info("All pharmacies have been updated!");
    }

    private static ArrayList<Pharmacy> getPharmacies() {
        // Pharmacy Ids seem to be from 4020 to 7920
        var ids = IntStream.range(4020, 7920).toArray();

        var listOfPharmacies = new ArrayList<Pharmacy>();

        Pharmacy pharmacy;

        for (var id : ids) {
            pharmacy = getSinglePharmacy(id);

            if (pharmacy == null) {
                continue;
            }

            listOfPharmacies.add(pharmacy);
        }

        return listOfPharmacies;
    }

    public static Pharmacy getSinglePharmacy(int id) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        final var tempUrl = "http://www.fsa.gr/pharmacyshow.asp?pharmacyid=";

        Document jsoupdoc;

        var url = tempUrl + id + "&programmeid=1";

        try {
            jsoupdoc = Jsoup.connect(url).get();
        } catch (Exception e) {
            return null;
        }

        var pharmacyInfo = getPharmacyInfoFromHTMLDOM(jsoupdoc);

        // transfer pharmacy info from tempArr to entity.Pharmacy class
        return new Pharmacy(Integer.parseInt(String.valueOf(id)),
                pharmacyInfo[0], pharmacyInfo[1], pharmacyInfo[2], pharmacyInfo[3]);
    }

    private static String[] getPharmacyInfoFromHTMLDOM(Document jsoupdoc) {
        var tempXPath = "html body center table tbody tr:eq(1) td table tbody tr";

        // temp String Array to temporarily save the pharmacy info taken from the web
        var pharmacyInfo = new String[4];
            /*
                1) entity.Pharmacy Name
                2) Address
                3) Area
                4) Phone Number
             */

        for (var i = 2; i <= 5; i++) {
            var xPath = tempXPath + ":eq(" + i + ")";
            var pageInfo = jsoupdoc.select(xPath).text();
            pageInfo = pageInfo.substring(pageInfo.indexOf(":") + 1).trim();
            pharmacyInfo[i - 2] = pageInfo;
        }

        return pharmacyInfo;
    }
}
