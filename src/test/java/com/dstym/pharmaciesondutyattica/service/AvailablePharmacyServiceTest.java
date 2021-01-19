package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"testing"})
@WebAppConfiguration
@SpringBootTest(classes = {PharmaciesOnDutyAtticaApplication.class})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Slf4j
class AvailablePharmacyServiceTest {
    @Autowired
    private AvailablePharmacyServiceImpl availablePharmacyService;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    @BeforeEach
    void beforeEach() {
        availablePharmacyRepository.deleteAll();
        createAvailablePharmacies();
    }

    private void createAvailablePharmacies() {
        var date = "2020-01-18";
        var pulledVersion = 1;

        var pharmacy1 = new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");
        var workingHour1 = new WorkingHour(3, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy1 = new AvailablePharmacy(100, pharmacy1, workingHour1, DateUtils.stringDateToInstant(date), pulledVersion);

        var pharmacy2 = new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876");
        var workingHour2 = new WorkingHour(19, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy2 = new AvailablePharmacy(101, pharmacy2, workingHour2, DateUtils.stringDateToInstant(date), pulledVersion);

        availablePharmacyRepository.saveAll(List.of(availablePharmacy1, availablePharmacy2));
    }

    private void assertAvailablePharmaciesProperties(AvailablePharmacy expectedAvailablePharmacy,
                                                     AvailablePharmacy actualAvailablePharmacy) {
        assertEquals(expectedAvailablePharmacy.getId(), actualAvailablePharmacy.getId());
        assertPharmaciesProperties(expectedAvailablePharmacy.getPharmacy(), actualAvailablePharmacy.getPharmacy());
        assertWorkingHoursProperties(expectedAvailablePharmacy.getWorkingHour(), actualAvailablePharmacy.getWorkingHour());
        assertEquals(expectedAvailablePharmacy.getDate(), actualAvailablePharmacy.getDate());
        assertEquals(expectedAvailablePharmacy.getPulledVersion(), actualAvailablePharmacy.getPulledVersion());
    }

    private void assertPharmaciesProperties(Pharmacy expectedPharmacy, Pharmacy actualPharmacy) {
        assertEquals(expectedPharmacy.getId(), actualPharmacy.getId());
        assertEquals(expectedPharmacy.getName(), actualPharmacy.getName());
        assertEquals(expectedPharmacy.getAddress(), actualPharmacy.getAddress());
        assertEquals(expectedPharmacy.getRegion(), actualPharmacy.getRegion());
        assertEquals(expectedPharmacy.getPhoneNumber(), actualPharmacy.getPhoneNumber());
    }

    private void assertWorkingHoursProperties(WorkingHour expectedWorkingHour, WorkingHour actualWorkingHour) {
        assertEquals(expectedWorkingHour.getId(), actualWorkingHour.getId());
        assertEquals(expectedWorkingHour.getWorkingHourText(), actualWorkingHour.getWorkingHourText());
    }

    @Test
    public void testFindAllByRegionAndDate_validDate_noRegionSpecified() {
        var date = availablePharmacyRepository.findAll().get(0).getDate();
        var availablePharmacies = availablePharmacyService.findAllByRegionAndDate(null, date, null);

        assertAvailablePharmaciesProperties(availablePharmacyService.findAll().get(0), availablePharmacies.getContent().get(0));
        assertAvailablePharmaciesProperties(availablePharmacyService.findAll().get(1), availablePharmacies.getContent().get(1));
    }

    @Test
    public void testFindAllByRegionAndDate_nonValidDate_noRegionSpecified() {
        var date = availablePharmacyRepository.findAll().get(0).getDate();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> availablePharmacyService
                .findAllByRegionAndDate("all", date, null));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus().value());
    }

    @Test
    public void testFindAllByRegionAndDate_validDate_nonValidRegionSpecified() {
        var date = availablePharmacyRepository.findAll().get(0).getDate();
        var region = "ΚΑΤΙ";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                availablePharmacyService.findAllByRegionAndDate(region, date, null));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus().value());
    }
}
