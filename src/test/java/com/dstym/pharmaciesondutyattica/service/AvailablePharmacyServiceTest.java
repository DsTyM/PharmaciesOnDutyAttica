package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AvailablePharmacyServiceTest {
    @InjectMocks
    private AvailablePharmacyServiceImpl availablePharmacyService;

    @Mock
    private AvailablePharmacyRepository availablePharmacyRepository;

    private static void assertAvailablePharmaciesProperties(AvailablePharmacy expectedAvailablePharmacy,
                                                            AvailablePharmacy actualAvailablePharmacy) {
        assertEquals(expectedAvailablePharmacy.getId(), actualAvailablePharmacy.getId());
        assertPharmaciesProperties(expectedAvailablePharmacy.getPharmacy(), actualAvailablePharmacy.getPharmacy());
        assertWorkingHoursProperties(expectedAvailablePharmacy.getWorkingHour(), actualAvailablePharmacy.getWorkingHour());
        assertEquals(expectedAvailablePharmacy.getDate(), actualAvailablePharmacy.getDate());
        assertEquals(expectedAvailablePharmacy.getPulledVersion(), actualAvailablePharmacy.getPulledVersion());
    }

    private static void assertPharmaciesProperties(Pharmacy expectedPharmacy, Pharmacy actualPharmacy) {
        assertEquals(expectedPharmacy.getId(), actualPharmacy.getId());
        assertEquals(expectedPharmacy.getName(), actualPharmacy.getName());
        assertEquals(expectedPharmacy.getAddress(), actualPharmacy.getAddress());
        assertEquals(expectedPharmacy.getRegion(), actualPharmacy.getRegion());
        assertEquals(expectedPharmacy.getPhoneNumber(), actualPharmacy.getPhoneNumber());
    }

    private static void assertWorkingHoursProperties(WorkingHour expectedWorkingHour, WorkingHour actualWorkingHour) {
        assertEquals(expectedWorkingHour.getId(), actualWorkingHour.getId());
        assertEquals(expectedWorkingHour.getWorkingHourText(), actualWorkingHour.getWorkingHourText());
    }

    @Test
    public void testFindAllByRegionAndDate_validDate_noRegionSpecified() {
        var date = "2020/01/18";
        var pulledVersion = 1;

        var pharmacy1 = new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");
        var workingHour1 = new WorkingHour(3, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy1 = new AvailablePharmacy(100, pharmacy1, workingHour1, date, pulledVersion);

        var pharmacy2 = new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876");
        var workingHour2 = new WorkingHour(19, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy2 = new AvailablePharmacy(101, pharmacy2, workingHour2, date, pulledVersion);

        when(availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date)).thenReturn(Collections.singletonList(
                new AvailablePharmacy(pulledVersion)
        ));

        // when no region is given, the service only runs findAllByRegionAndDate()
        // that's why we only mock this
        when(availablePharmacyRepository.findAllByLastPulledVersion(pulledVersion, date, null, null))
                .thenReturn(
                        new PageImpl<>(Arrays.asList(
                                availablePharmacy1, availablePharmacy2
                        )));

        var availablePharmacies = availablePharmacyService.findAllByRegionAndDate(null, date, null);

        assertAvailablePharmaciesProperties(availablePharmacy1, availablePharmacies.getContent().get(0));
        assertAvailablePharmaciesProperties(availablePharmacy2, availablePharmacies.getContent().get(1));
    }

    @Test
    public void testFindAllByRegionAndDate_nonValidDate_noRegionSpecified() {
        var date = "18/1/2020";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            availablePharmacyService.findAllByRegionAndDate("all", date, null);
        });

        String expectedMessage = "Did not find available pharmacies for date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindAllByRegionAndDate_validDate_nonValidRegionSpecified() {
        var date = "2020/01/18";
        var pulledVersion = 1;
        var region = "ΠΑΓΚΡΑΤΙ";

        when(availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date)).thenReturn(Collections.singletonList(
                new AvailablePharmacy(pulledVersion)
        ));

        // We give an available pharmacy without any information, and without region,
        // so it will returns no results for the given region,
        // but it will not stop on findAllByRegionAndDate(), because,
        // findAllByRegionAndDate() will return an empty List, so
        // it will think that the date is correct
        when(availablePharmacyRepository.findAllByLastPulledVersion(pulledVersion, date, null, null))
                .thenReturn(
                        new PageImpl<>(Collections.emptyList()
                        ));

        Exception exception = assertThrows(RuntimeException.class, () ->
                availablePharmacyService.findAllByRegionAndDate(region, date, null));

        String expectedMessage = "Cannot invoke \"org.springframework.data.domain.Page.isEmpty()\" because \"result\" is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
