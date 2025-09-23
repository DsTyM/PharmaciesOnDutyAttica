package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.PharmaciesOnDutyAtticaApplication;
import com.dstym.pharmaciesondutyattica.TestcontainersConfiguration;
import com.dstym.pharmaciesondutyattica.mapper.WorkingHourMapper;
import com.dstym.pharmaciesondutyattica.model.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
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
class WorkingHourServiceTest {
    @Autowired
    private WorkingHourService workingHourService;

    @Autowired
    private WorkingHourRepository workingHourRepository;

    @Autowired
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    private WorkingHourMapper workingHourMapper;

    @BeforeEach
    void beforeEach() {
        availablePharmacyRepository.deleteAll();
        workingHourRepository.deleteAll();

        createWorkingHours();
    }

    private void createWorkingHours() {
        var workingHour1 = new WorkingHour(null, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var workingHour2 = new WorkingHour(null, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        workingHourRepository.saveAll(List.of(workingHour1, workingHour2));
    }

    private void assertWorkingHoursProperties(WorkingHour expectedWorkingHour, WorkingHour actualWorkingHour) {
        assertEquals(expectedWorkingHour.getId(), actualWorkingHour.getId());
        assertEquals(expectedWorkingHour.getWorkingHourText(), actualWorkingHour.getWorkingHourText());
    }

    @Test
    public void testGetWorkingHours() {
        var workingHoursDtoPage = workingHourService.getWorkingHours(Pageable.unpaged());
        var workingHours = workingHoursDtoPage.map(workingHourMapper::getworkingHour).toList();

        assertEquals(workingHourRepository.findAll().size(), workingHoursDtoPage.getContent().size());
        assertWorkingHoursProperties(workingHourRepository.findAll().get(0), workingHours.get(0));
        assertWorkingHoursProperties(workingHourRepository.findAll().get(1), workingHours.get(1));
    }

    @Test
    public void testGetWorkingHour_validId() {
        var id = workingHourRepository.findAll().getFirst().getId();
        var workingHour = workingHourMapper.getworkingHour(workingHourService.getWorkingHour(id));

        assertWorkingHoursProperties(workingHourRepository.findById(id).orElseThrow(), workingHour);
    }

    @Test
    public void testGetWorkingHour_nonValidId() {
        var id = workingHourRepository.findAll().get(this.workingHourRepository.findAll().size() - 1).getId() + 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> workingHourService.getWorkingHour(id));

        assertEquals(HttpStatus.NOT_FOUND.toString(), exception.getStatusCode().toString());
    }
}
