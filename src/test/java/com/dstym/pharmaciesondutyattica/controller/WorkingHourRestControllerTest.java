package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WorkingHourRestController.class)
class WorkingHourRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkingHourService workingHourService;

    @Test
    public void testGetWorkingHours() throws Exception {
        when(workingHourService.findAll()).thenReturn(
                Arrays.asList(
                        new WorkingHour(3, "8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ"),
                        new WorkingHour(19, "8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ"))
        );

        var request = MockMvcRequestBuilders
                .get("/api/working-hours")
                .accept(MediaType.APPLICATION_JSON);

        var json = "[" +
                "{\n" +
                "        \"id\": 3,\n" +
                "        \"workingHourText\": \"8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 19,\n" +
                "        \"workingHourText\": \"8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ\"\n" +
                "}" +
                "]";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }
}
