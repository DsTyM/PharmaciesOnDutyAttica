package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.TestcontainersConfiguration;
import com.dstym.pharmaciesondutyattica.mapper.PharmacyMapper;
import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
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
@Import(TestcontainersConfiguration.class)
class PharmacyServiceTest {
    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    private PharmacyMapper pharmacyMapper;

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
        var pharmacy1 = new Pharmacy(null, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147",
                "ΠΑΓΚΡΑΤΙ", "22123 12345");
        var pharmacy2 = new Pharmacy(null, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1",
                "ΘΗΣΕΙΟ", "223430 9876");
        pharmacyRepository.saveAll(List.of(pharmacy1, pharmacy2));
    }

    @Test
    public void testGetPharmacies() {
        var pharmaciesDtoPage = pharmacyService.getPharmacies(null, Pageable.unpaged());
        var pharmacies = pharmaciesDtoPage.map(pharmacyMapper::getPharmacy).toList();

        assertEquals(pharmacyRepository.findAll().size(), pharmacies.size());
        assertPharmaciesProperties(pharmacyRepository.findAll().get(0), pharmacies.get(0));
        assertPharmaciesProperties(pharmacyRepository.findAll().get(1), pharmacies.get(1));
    }

    @Test
    public void testGetPharmacy_validId() {
        var id = pharmacyRepository.findAll().getFirst().getId();
        var pharmacy = pharmacyMapper.getPharmacy(pharmacyService.getPharmacy(id));

        assertPharmaciesProperties(pharmacyRepository.findById(id).orElseThrow(), pharmacy);
    }

    @Test
    public void testGetPharmacy_nonValidId() {
        int id = pharmacyRepository.findAll().get(this.pharmacyRepository.findAll().size() - 1).getId() + 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> pharmacyService.getPharmacy(id));

        assertEquals(HttpStatus.NOT_FOUND.toString(), exception.getStatusCode().toString());
    }
}
