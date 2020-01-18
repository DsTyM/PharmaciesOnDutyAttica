package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PharmacyServiceTest {
    @InjectMocks
    private PharmacyServiceImpl pharmacyService;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Test
    public void testFindAll() {
        var pharmacy1 = new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");

        var pharmacy2 = new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876");

        when(pharmacyRepository.findAll()).thenReturn(Arrays.asList(
                pharmacy1, pharmacy2
        ));

        var pharmacies = pharmacyService.findAll();

        assertPharmaciesProperties(pharmacy1, pharmacies.get(0));
        assertPharmaciesProperties(pharmacy2, pharmacies.get(1));
    }

    @Test
    public void testFindAll_noResults() {
        when(pharmacyRepository.findAll()).thenReturn(new ArrayList<>());

        var pharmacies = pharmacyService.findAll();

        assertEquals(pharmacies, new ArrayList<>());
    }

    @Test
    public void testFindById_validId() {
        var id = 4050;
        var expectedPharmacy = new Pharmacy(id, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");

        when(pharmacyRepository.findById(id)).thenReturn(
                Optional.of(expectedPharmacy)
        );

        var actualPharmacy = pharmacyService.findById(id);

        assertPharmaciesProperties(expectedPharmacy, actualPharmacy);
    }

    @Test
    public void testFindById_nonValidId() {
        int id = -1;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pharmacyService.findById(id);
        });

        String expectedMessage = "Did not find pharmacy with id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private static void assertPharmaciesProperties(Pharmacy expectedPharmacy, Pharmacy actualPharmacy) {
        assertEquals(expectedPharmacy.getId(), actualPharmacy.getId());
        assertEquals(expectedPharmacy.getName(), actualPharmacy.getName());
        assertEquals(expectedPharmacy.getAddress(), actualPharmacy.getAddress());
        assertEquals(expectedPharmacy.getRegion(), actualPharmacy.getRegion());
        assertEquals(expectedPharmacy.getPhoneNumber(), actualPharmacy.getPhoneNumber());
    }
}
