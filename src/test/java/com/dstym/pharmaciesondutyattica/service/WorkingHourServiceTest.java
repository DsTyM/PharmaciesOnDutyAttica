package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
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
class WorkingHourServiceTest {
    @Autowired
    private WorkingHourService workingHourService;

    @Autowired
    private WorkingHourRepository workingHourRepository;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    @BeforeEach
    void beforeEach() {
        availablePharmacyRepository.deleteAll();
        workingHourRepository.deleteAll();

        createWorkingHours();
    }

    private void createWorkingHours() {
        workingHourRepository.save(new WorkingHour(0, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ"));
        workingHourRepository.save(new WorkingHour(0, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ"));
    }

    private void assertWorkingHoursProperties(WorkingHour expectedWorkingHour, WorkingHour actualWorkingHour) {
        assertEquals(expectedWorkingHour.getId(), actualWorkingHour.getId());
        assertEquals(expectedWorkingHour.getWorkingHourText(), actualWorkingHour.getWorkingHourText());
    }

    @Test
    public void testFindAll() {
        var workingHours = workingHourService.findAll(null);

        assertEquals(workingHourRepository.findAll().size(), workingHours.getContent().size());
        assertWorkingHoursProperties(workingHourRepository.findAll().get(0), workingHours.getContent().get(0));
        assertWorkingHoursProperties(workingHourRepository.findAll().get(1), workingHours.getContent().get(1));
    }

    @Test
    public void testFindById_validId() {
        var id = workingHourRepository.findAll().get(0).getId();

        assertWorkingHoursProperties(workingHourRepository.findById(id).get(), workingHourService.findById(id));
    }

    @Test
    public void testFindById_nonValidId() {
        var id = workingHourRepository.findAll().get(this.workingHourRepository.findAll().size() - 1).getId() + 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> workingHourService.findById(id));

        assertEquals(HttpStatus.NOT_FOUND.toString(), exception.getStatusCode().toString());
    }
}
