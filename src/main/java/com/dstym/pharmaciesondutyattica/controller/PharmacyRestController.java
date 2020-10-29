package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PharmacyRestController {
    private final PharmacyService pharmacyService;

    @Autowired
    public PharmacyRestController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/pharmacies")
    @ResponseBody
    @Operation(summary = "Get Pharmacies",
            description = "Get the List of the Pharmacies in the Database.")
    public ResponseEntity<Page<Pharmacy>> getPharmacies(
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region,
            @Parameter(description = "Specify page.")
            @RequestParam(required = false) String page,
            @Parameter(description = "Specify size.")
            @RequestParam(required = false) String size,
            @Parameter(hidden = true) @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(pharmacyService.findAll(region, pageable));
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    @Operation(summary = "Find a Pharmacy by Id",
            description = "Returns a specific Pharmacy info by the given Id.")
    public ResponseEntity<Pharmacy> getPharmacy(
            @Parameter(description = "Specify the Pharmacy ID.")
            @PathVariable int pharmacyId) {
        return ResponseEntity.ok(pharmacyService.findById(pharmacyId));
    }
}
