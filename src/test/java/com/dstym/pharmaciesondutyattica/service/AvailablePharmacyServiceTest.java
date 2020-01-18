package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AvailablePharmacyServiceTest {
    @InjectMocks
    private AvailablePharmacyServiceImpl availablePharmacyService;

    @Mock
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Test
    public void testFindAllByRegionAndDate_validDate_noRegion() {
        var date = "18/1/2020";
        var pulledVersion = 1;

        var pharmacy1 = new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");
        var workingHour1 = new WorkingHour(3, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy1 = new AvailablePharmacy(100, pharmacy1, workingHour1, date, pulledVersion);

        var pharmacy2 = new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876");
        var workingHour2 = new WorkingHour(19, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var availablePharmacy2 = new AvailablePharmacy(101, pharmacy2, workingHour2, date, pulledVersion);

        when(availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date)).thenReturn(Arrays.asList(
                new AvailablePharmacy(pulledVersion)
        ));

        // when no region is given, the service only runs findByDateAndAndPulledVersion()
        // that's why we only mock this
        when(availablePharmacyRepository.findByDateAndAndPulledVersion(date, pulledVersion)).thenReturn(Arrays.asList(
                availablePharmacy1, availablePharmacy2
        ));

        var availablePharmacies = availablePharmacyService.findAllByRegionAndDate("all", date);

        /*
            I should add all assertEquals() to assertAvailablePharmaciesProperties().
            I should also create assertPharmaciesProperties() and assertWorkingHoursProperties()
            I should save new Objects to tempVariables for cleaner code.
            I need to do this to all of my previous tests.
         */

        assertAvailablePharmaciesProperties(availablePharmacy1, availablePharmacies.get(0));
        assertAvailablePharmaciesProperties(availablePharmacy2, availablePharmacies.get(1));
    }

    private static void assertAvailablePharmaciesProperties(AvailablePharmacy ap1, AvailablePharmacy ap2) {
        assertEquals(ap1.getId(), ap2.getId());
        assertPharmaciesProperties(ap1.getPharmacy(), ap2.getPharmacy());
        assertWorkingHoursProperties(ap1.getWorkingHour(), ap2.getWorkingHour());
        assertEquals(ap1.getDate(), ap2.getDate());
        assertEquals(ap1.getPulledVersion(), ap2.getPulledVersion());
    }

    private static void assertPharmaciesProperties(Pharmacy p1, Pharmacy p2) {
        assertEquals(p1.getId(), p2.getId());
        assertEquals(p1.getName(), p2.getName());
        assertEquals(p1.getAddress(), p2.getAddress());
        assertEquals(p1.getRegion(), p2.getRegion());
        assertEquals(p1.getPhoneNumber(), p2.getPhoneNumber());
    }

    private static void assertWorkingHoursProperties(WorkingHour wh1, WorkingHour wh2) {
        assertEquals(wh1.getId(), wh2.getId());
        assertEquals(wh1.getWorkingHourText(), wh2.getWorkingHourText());
    }
}
