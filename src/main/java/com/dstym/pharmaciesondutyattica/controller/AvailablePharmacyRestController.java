package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AvailablePharmacyRestController {
    private AvailablePharmacyService availablePharmacyService;

    @Autowired
    public AvailablePharmacyRestController(AvailablePharmacyService availablePharmacyService) {
        this.availablePharmacyService = availablePharmacyService;
    }

    @GetMapping("/available-pharmacies")
    @Operation(summary = "Get Pharmacies On Duty",
            description = "Get the List of the Pharmacies on Duty. Today is the default date.")
    public List<AvailablePharmacy> GetAvailablePharmacies(
            @Parameter(description = "Specify the date (Date Format: D-M-YYYY).")
            @RequestParam(required = false) String date,
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region) {
        if (date == null || date.equals("")) {
            date = "today";
        }

        if (region == null || region.equals("")) {
            region = "all";
        }

        return availablePharmacyService.findAllByRegionAndDate(region, date);
    }
}
