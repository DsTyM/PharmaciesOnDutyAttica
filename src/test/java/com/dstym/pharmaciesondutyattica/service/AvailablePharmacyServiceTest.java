package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.TestcontainersConfiguration;
import com.dstym.pharmaciesondutyattica.mapper.AvailablePharmacyMapper;
import com.dstym.pharmaciesondutyattica.model.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import com.dstym.pharmaciesondutyattica.model.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest(classes = {PharmaciesOnDutyAtticaApplication.class})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
class AvailablePharmacyServiceTest {
    @Autowired
    private AvailablePharmacyService availablePharmacyService;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    private WorkingHourRepository workingHourRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private AvailablePharmacyMapper availablePharmacyMapper;

    @BeforeEach
    void beforeEach() {
        availablePharmacyRepository.deleteAll();
        createAvailablePharmacies();
    }

    private void createAvailablePharmacies() {
        var date = "2020-01-18";
        var pulledVersion = 1;

        var pharmacy1 = new Pharmacy(null, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", "ΠΑΓΚΡΑΤΙ", "22123 12345");
        var workingHour1 = new WorkingHour(null, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy1 = new AvailablePharmacy(null, pharmacy1, workingHour1, DateUtils.stringDateToInstant(date), pulledVersion);

        var pharmacy2 = new Pharmacy(null, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1", "ΘΗΣΕΙΟ", "223430 9876");
        var workingHour2 = new WorkingHour(null, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy2 = new AvailablePharmacy(null, pharmacy2, workingHour2, DateUtils.stringDateToInstant(date), pulledVersion);

        workingHourRepository.saveAll(List.of(workingHour1, workingHour2));
        pharmacyRepository.saveAll(List.of(pharmacy1, pharmacy2));

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
    public void testGetPharmacies_validDate_noRegionSpecified() {
        var date = availablePharmacyRepository.findAll().getFirst().getDate();
        var availablePharmaciesDtoPage = availablePharmacyService.getAvailablePharmacies(null, date, Pageable.unpaged());
        var availablePharmacies = availablePharmaciesDtoPage.map(availablePharmacyMapper::getAvailablePharmacy).toList();


        assertAvailablePharmaciesProperties(availablePharmacyRepository.findAll().get(0), availablePharmacies.get(0));
        assertAvailablePharmaciesProperties(availablePharmacyRepository.findAll().get(1), availablePharmacies.get(1));
    }

    @Test
    public void testGetPharmacies_nonValidDate_noRegionSpecified() {
        var date = availablePharmacyRepository.findAll().getFirst().getDate();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> availablePharmacyService
                .getAvailablePharmacies("all", date, Pageable.unpaged()));

        assertEquals(HttpStatus.NOT_FOUND.toString(), exception.getStatusCode().toString());
    }

    @Test
    public void testGetPharmacies_validDate_nonValidRegionSpecified() {
        var date = availablePharmacyRepository.findAll().getFirst().getDate();
        var region = "ΚΑΤΙ";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                availablePharmacyService.getAvailablePharmacies(region, date, Pageable.unpaged()));

        assertEquals(HttpStatus.NOT_FOUND.toString(), exception.getStatusCode().toString());
    }
}
