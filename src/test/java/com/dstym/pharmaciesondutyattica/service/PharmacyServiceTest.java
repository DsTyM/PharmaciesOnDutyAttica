package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"testing"})
@WebAppConfiguration
@SpringBootTest(classes = {PharmaciesOnDutyAtticaApplication.class})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PharmacyServiceTest {
    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    private static void assertPharmaciesProperties(Pharmacy expectedPharmacy, Pharmacy actualPharmacy) {
        assertEquals(expectedPharmacy.getId(), actualPharmacy.getId());
        assertEquals(expectedPharmacy.getName(), actualPharmacy.getName());
        assertEquals(expectedPharmacy.getAddress(), actualPharmacy.getAddress());
        assertEquals(expectedPharmacy.getRegion(), actualPharmacy.getRegion());
        assertEquals(expectedPharmacy.getPhoneNumber(), actualPharmacy.getPhoneNumber());
    }

    @BeforeEach
    void beforeEach() {
        availablePharmacyRepository.deleteAll();
        pharmacyRepository.deleteAll();

        createPharmacies();
    }

    private void createPharmacies() {
        pharmacyRepository.save(new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345"));
        pharmacyRepository.save(new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876"));
    }

    @Test
    public void testFindAll() {
        var pharmacies = pharmacyService.findAll(null, null);

        assertEquals(pharmacyRepository.findAll().size(), pharmacies.getContent().size());
        assertPharmaciesProperties(pharmacyRepository.findAll().get(0), pharmacies.getContent().get(0));
        assertPharmaciesProperties(pharmacyRepository.findAll().get(1), pharmacies.getContent().get(1));
    }

    @Test
    public void testFindById_validId() {
        var id = pharmacyRepository.findAll().get(0).getId();

        assertPharmaciesProperties(pharmacyRepository.findById(id).get(), pharmacyService.findById(id));
    }

    @Test
    public void testFindById_nonValidId() {
        int id = pharmacyRepository.findAll().get(this.pharmacyRepository.findAll().size() - 1).getId() + 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> pharmacyService.findById(id));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus().value());
    }
}
