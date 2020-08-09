package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.assembler.PharmacyAssembler;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PharmacyRestController {
    private final PharmacyService pharmacyService;
    private final PharmacyAssembler pharmacyAssembler;
    private final PagedResourcesAssembler<Pharmacy> pagedResourcesAssembler;

    @Autowired
    public PharmacyRestController(PharmacyService pharmacyService,
                                  PharmacyAssembler pharmacyAssembler,
                                  PagedResourcesAssembler<Pharmacy> pagedResourcesAssembler) {
        this.pharmacyService = pharmacyService;
        this.pharmacyAssembler = pharmacyAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/pharmacies")
    @Operation(summary = "Get Pharmacies", description = "Get the List of the Pharmacies in the Database.")
    public ResponseEntity<PagedModel<Pharmacy>> getPharmacies(
            @Parameter(hidden = true) @PageableDefault(value = 50) Pageable pageable,
            @Parameter(description = "Specify a region.") @RequestParam(required = false) String region) {
        Page<Pharmacy> pharmacies;
        if (region != null && !region.trim().equals("")) {
            pharmacies = pharmacyService.findByRegion(region.trim(), pageable);
        } else {
            pharmacies = pharmacyService.findAll(pageable);
        }

        PagedModel<Pharmacy> pharmacyModel = pagedResourcesAssembler
                .toModel(pharmacies, pharmacyAssembler);

        return new ResponseEntity<>(pharmacyModel, HttpStatus.OK);
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    @Operation(summary = "Find a Pharmacy by Id", description = "Returns a specific Pharmacy info by the given Id.")
    public Pharmacy getPharmacy(
            @Parameter(description = "Specify the Pharmacy ID.")
            @PathVariable int pharmacyId) {
        return pharmacyService.findById(pharmacyId);
    }
}
