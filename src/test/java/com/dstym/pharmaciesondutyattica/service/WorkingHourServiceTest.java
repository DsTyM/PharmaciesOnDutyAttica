package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
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
class WorkingHourServiceTest {
    @InjectMocks
    private WorkingHourServiceImpl workingHourService;

    @Mock
    private WorkingHourRepository workingHourRepository;

    private static void assertWorkingHoursProperties(WorkingHour expectedWorkingHour, WorkingHour actualWorkingHour) {
        assertEquals(expectedWorkingHour.getId(), actualWorkingHour.getId());
        assertEquals(expectedWorkingHour.getWorkingHourText(), actualWorkingHour.getWorkingHourText());
    }

    @Test
    public void testFindAll() {
        var workingHour1 = new WorkingHour(3, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");
        var workingHour2 = new WorkingHour(19, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ");

        when(workingHourRepository.findAll()).thenReturn(Arrays.asList(
                workingHour1, workingHour2
        ));

        var workingHours = workingHourService.findAll();

        assertWorkingHoursProperties(workingHour1, workingHours.get(0));
        assertWorkingHoursProperties(workingHour2, workingHours.get(1));
    }

    @Test
    public void testFindAll_noResults() {
        when(workingHourRepository.findAll()).thenReturn(new ArrayList<>());

        var workingHours = workingHourService.findAll();

        assertEquals(workingHours, new ArrayList<>());
    }

    @Test
    public void testFindById_validId() {
        var id = 3;
        var expectedWorkingHour = new WorkingHour(id, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ");

        when(workingHourRepository.findById(id)).thenReturn(
                Optional.of(expectedWorkingHour)
        );

        var actualWorkingHour = workingHourService.findById(id);

        assertWorkingHoursProperties(expectedWorkingHour, actualWorkingHour);
    }

    @Test
    public void testFindById_nonValidId() {
        int id = -1;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            workingHourService.findById(id);
        });

        String expectedMessage = "Did not find workingHour with id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}