package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AvailablePharmacyRestController.class)
class AvailablePharmacyRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailablePharmacyService availablePharmacyService;

    @Test
    public void testGetAvailablePharmacies_noArgs() throws Exception {
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

        when(availablePharmacyService.findAllByRegionAndDate("all", "today")).thenReturn(
                Arrays.asList(
                        availablePharmacy1, availablePharmacy2
                ));

        var request = MockMvcRequestBuilders
                .get("/api/available-pharmacies/")
                .accept(MediaType.APPLICATION_JSON);

        var json = "[\n" +
                "  {\n" +
                "    \"id\": 100,\n" +
                "    \"pharmacy\": {\n" +
                "      \"id\": 4050,\n" +
                "      \"name\": \"ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ\",\n" +
                "      \"address\": \"ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147\",\n" +
                "      \"region\": \"ΠΑΓΚΡΑΤΙ\",\n" +
                "      \"phoneNumber\": \"22123 12345\"\n" +
                "    },\n" +
                "    \"workingHour\": {\n" +
                "      \"id\": 3,\n" +
                "      \"workingHourText\": \"8 ΤΟ ΠΡΩΙ ΜΕ 9 ΤΟ ΒΡΑΔΥ\"\n" +
                "    },\n" +
                "    \"date\": \"18/1/2020\",\n" +
                "    \"pulledVersion\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 101,\n" +
                "    \"pharmacy\": {\n" +
                "      \"id\": 6017,\n" +
                "      \"name\": \"ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ\",\n" +
                "      \"address\": \"ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1\",\n" +
                "      \"region\": \"ΘΗΣΕΙΟ\",\n" +
                "      \"phoneNumber\": \"223430 9876\"\n" +
                "    },\n" +
                "    \"workingHour\": {\n" +
                "      \"id\": 19,\n" +
                "      \"workingHourText\": \"8 ΤΟ ΠΡΩΙ ΜΕ 2 ΤΟ ΜΕΣΗΜΕΡΙ ΚΑΙ 5 ΤΟ ΑΠΟΓΕΥΜΑ ΜΕ 9 ΤΟ ΒΡΑΔΥ\"\n" +
                "    },\n" +
                "    \"date\": \"18/1/2020\",\n" +
                "    \"pulledVersion\": 1\n" +
                "  }\n" +
                "]\n";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }

    @Test
    public void testGetAvailablePharmacies_noArgs_noResults() throws Exception {
        when(availablePharmacyService.findAllByRegionAndDate("all", "today")).thenReturn(new ArrayList<>());

        var request = MockMvcRequestBuilders
                .get("/api/available-pharmacies")
                .accept(MediaType.APPLICATION_JSON);

        // empty array because there are no results
        var json = "[]";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }
}
