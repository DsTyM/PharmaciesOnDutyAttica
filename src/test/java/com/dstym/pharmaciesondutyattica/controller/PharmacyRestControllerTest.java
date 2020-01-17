package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
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
@WebMvcTest(PharmacyRestController.class)
class PharmacyRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacyService pharmacyService;

    @Test
    public void testGetPharmacies() throws Exception {
        when(pharmacyService.findAll()).thenReturn(
                Arrays.asList(
                        new Pharmacy(4050, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", "ΠΑΓΚΡΑΤΙ",
                                "22123 12345"),
                        new Pharmacy(6017, "ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ", "ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1", "ΘΗΣΕΙΟ",
                                "223430 9876")
                ));

        var request = MockMvcRequestBuilders
                .get("/api/pharmacies")
                .accept(MediaType.APPLICATION_JSON);

        var json = "[\n" +
                "    {\n" +
                "        \"id\": 4050,\n" +
                "        \"name\": \"ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ\",\n" +
                "        \"address\": \"ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147\",\n" +
                "        \"region\": \"ΠΑΓΚΡΑΤΙ\",\n" +
                "        \"phoneNumber\": \"22123 12345\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 6017,\n" +
                "        \"name\": \"ΠΕΤΡΟΣ ΠΑΠΑΝΙΚΟΛΑΣ\",\n" +
                "        \"address\": \"ΧΡΗΣΤΟΥ ΜΟΝΤΕΧΡΗΣΤΟΥ 1\",\n" +
                "        \"region\": \"ΘΗΣΕΙΟ\",\n" +
                "        \"phoneNumber\": \"223430 9876\"\n" +
                "    }\n" +
                "]";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }

    @Test
    public void testGetPharmacies_noResults() throws Exception {
        when(pharmacyService.findAll()).thenReturn(new ArrayList<>());

        var request = MockMvcRequestBuilders
                .get("/api/pharmacies")
                .accept(MediaType.APPLICATION_JSON);

        // empty array because there are no results
        var json = "[]";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }

    @Test
    public void testGetPharmacy_validId() throws Exception {
        int id = 4050;

        when(pharmacyService.findById(id)).thenReturn(
                new Pharmacy(id, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", "ΠΑΓΚΡΑΤΙ",
                        "22123 12345")
        );

        var request = MockMvcRequestBuilders
                .get("/api/pharmacies/" + id)
                .accept(MediaType.APPLICATION_JSON);

        var json = "{\n" +
                "        \"id\": 4050,\n" +
                "        \"name\": \"ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ\",\n" +
                "        \"address\": \"ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147\",\n" +
                "        \"region\": \"ΠΑΓΚΡΑΤΙ\",\n" +
                "        \"phoneNumber\": \"22123 12345\"\n" +
                "}";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }

    @Test
    public void testGetPharmacy_nonValidId() throws Exception {
        int id = -1;

        when(pharmacyService.findById(id)).thenReturn(
                new Pharmacy(id, "ΣΠΥΡΟΣ ΝΙΚΟΛΑΚΟΠΟΥΛΟΣ", "ΠΕΤΡΟΥ ΚΑΡΑΓΙΩΡΓΟΥ 147", "ΠΑΓΚΡΑΤΙ",
                        "22123 12345")
        );

        var request = MockMvcRequestBuilders
                .get("/api/pharmacies/" + id)
                .accept(MediaType.APPLICATION_JSON);

        // we expect an empty json file
        var json = "{}";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andReturn();
    }
}
