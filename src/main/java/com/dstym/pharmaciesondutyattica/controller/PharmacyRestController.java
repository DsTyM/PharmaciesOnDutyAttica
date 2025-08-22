package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Pharmacy")
public class PharmacyRestController {
    private final PharmacyService pharmacyService;

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
            @Parameter(description = "Select property name for sorting.", schema = @Schema(
                    allowableValues = {"name", "name,DESC",
                            "address", "address,DESC",
                            "region", "region,DESC"}))
            @RequestParam(required = false) String sort,
            @Parameter(hidden = true) @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(pharmacyService.findAll(region, pageable));
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    @Operation(summary = "Find a Pharmacy by Id",
            description = "Returns a specific Pharmacy info by the given Id.")
    public ResponseEntity<Pharmacy> getPharmacy(
            @Parameter(description = "Specify the Pharmacy ID.")
            @PathVariable Integer pharmacyId) {
        return ResponseEntity.ok(pharmacyService.findById(pharmacyId));
    }
}
