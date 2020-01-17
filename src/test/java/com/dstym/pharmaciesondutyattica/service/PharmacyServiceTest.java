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
        when(pharmacyRepository.findAll()).thenReturn(Arrays.asList(
                new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", "ΠΑΓΚΡΑΤΙ",
                        "22123 12345"),
                new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1", "ΘΗΣΕΙΟ",
                        "223430 9876")
        ));

        var pharmacies = pharmacyService.findAll();

        assertEquals(4050, pharmacies.get(0).getId());
        assertEquals("ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", pharmacies.get(0).getName());
        assertEquals("ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", pharmacies.get(0).getAddress());
        assertEquals("ΠΑΓΚΡΑΤΙ", pharmacies.get(0).getRegion());
        assertEquals("22123 12345", pharmacies.get(0).getPhoneNumber());

        assertEquals(6017, pharmacies.get(1).getId());
        assertEquals("ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", pharmacies.get(1).getName());
        assertEquals("ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1", pharmacies.get(1).getAddress());
        assertEquals("ΘΗΣΕΙΟ", pharmacies.get(1).getRegion());
        assertEquals("223430 9876", pharmacies.get(1).getPhoneNumber());
    }

    @Test
    public void testFindAll_noResults() {
        when(pharmacyRepository.findAll()).thenReturn(new ArrayList<>());

        var pharmacies = pharmacyService.findAll();

        assertEquals(pharmacies, new ArrayList<>());
    }

    @Test
    public void testFindById_validId() {
        int id = 4050;
        when(pharmacyRepository.findById(id)).thenReturn(
                Optional.of(new Pharmacy(id, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                        "ΠΑΓΚΡΑΤΙ", "22123 12345"))
        );

        var pharmacy = pharmacyService.findById(id);

        assertEquals(id, pharmacy.getId());
        assertEquals("ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", pharmacy.getName());
        assertEquals("ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", pharmacy.getAddress());
        assertEquals("ΠΑΓΚΡΑΤΙ", pharmacy.getRegion());
        assertEquals("22123 12345", pharmacy.getPhoneNumber());
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
}
