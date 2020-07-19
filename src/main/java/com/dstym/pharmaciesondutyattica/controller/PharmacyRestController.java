package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PharmacyRestController {
    private PharmacyService pharmacyService;

    @Autowired
    public PharmacyRestController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/pharmacies")
    @ResponseBody
    @Operation(summary = "Get Pharmacies",
            description = "Get the List of the Pharmacies in the Database.")
    public List<Pharmacy> getPharmacies(
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region) {
        if (region != null && !region.trim().equals("")) {
            return pharmacyService.findByRegion(region.trim());
        }

        return pharmacyService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    @Operation(summary = "Find a Pharmacy by Id",
            description = "Returns a specific Pharmacy info by the given Id.")
    public Pharmacy getPharmacy(
            @Parameter(description = "Specify the Pharmacy ID.")
            @PathVariable int pharmacyId) {
        return pharmacyService.findById(pharmacyId);
    }
}
