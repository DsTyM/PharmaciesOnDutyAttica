package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
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
public class AvailablePharmacyRestController {
    private final AvailablePharmacyService availablePharmacyService;

    @Autowired
    public AvailablePharmacyRestController(AvailablePharmacyService availablePharmacyService) {
        this.availablePharmacyService = availablePharmacyService;
    }

    @GetMapping("/available-pharmacies")
    @Operation(summary = "Get Pharmacies On Duty",
            description = "Get the List of the Pharmacies on Duty. Today is the default date.")
    public ResponseEntity<Page<AvailablePharmacy>> GetAvailablePharmacies(
            @Parameter(description = "Specify the date (Date Format: D-M-YYYY).")
            @RequestParam(required = false) String date,
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region,
            @Parameter(description = "Specify page.")
            @RequestParam(required = false) String page,
            @Parameter(description = "Specify size.")
            @RequestParam(required = false) String size,
            @Parameter(hidden = true) @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(availablePharmacyService.findAllByRegionAndDate(region, date, pageable));
    }
}
