package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.assembler.AvailablePharmacyAssembler;
import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AvailablePharmacyRestController {
    private final AvailablePharmacyService availablePharmacyService;
    private final AvailablePharmacyAssembler availablePharmacyAssembler;
    private final PagedResourcesAssembler<AvailablePharmacy> pagedResourcesAssembler;

    @Autowired
    public AvailablePharmacyRestController(AvailablePharmacyService availablePharmacyService,
                                           AvailablePharmacyAssembler availablePharmacyAssembler,
                                           PagedResourcesAssembler<AvailablePharmacy> pagedResourcesAssembler) {
        this.availablePharmacyService = availablePharmacyService;
        this.availablePharmacyAssembler = availablePharmacyAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/available-pharmacies")
    @Operation(summary = "Get Pharmacies On Duty",
            description = "Get the List of the Pharmacies on Duty. Today is the default date.")
    public ResponseEntity<PagedModel<AvailablePharmacy>> GetAvailablePharmacies(
            @Parameter(hidden = true) @PageableDefault(value = 200) Pageable pageable,
            @Parameter(description = "Specify the date (Date Format: D-M-YYYY).")
            @RequestParam(required = false) String date,
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region) {
        if (date == null || date.trim().equals("")) {
            date = "today";
        }

        if (region == null || region.trim().equals("")) {
            region = "all";
        }

        var availablePharmacies = availablePharmacyService.findAllByRegionAndDate(
                region, date, pageable);

        PagedModel<AvailablePharmacy> availablePharmacyModel = pagedResourcesAssembler
                .toModel(availablePharmacies, availablePharmacyAssembler);

        return new ResponseEntity<>(availablePharmacyModel, HttpStatus.OK);
    }
}
