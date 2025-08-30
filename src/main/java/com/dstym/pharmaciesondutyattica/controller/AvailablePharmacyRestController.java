package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.dto.AvailablePharmacyDto;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Available Pharmacy")
public class AvailablePharmacyRestController {
    private final AvailablePharmacyService availablePharmacyService;

    @GetMapping("/available-pharmacies")
    @Operation(summary = "Get Pharmacies On Duty",
            description = "Get the List of the Pharmacies on Duty. Today is the default date.")
    public ResponseEntity<Page<AvailablePharmacyDto>> getAvailablePharmacies(
            @Parameter(description = "Specify the date (Date Format: YYYY-MM-DD).")
            @RequestParam(required = false) String date,
            @Parameter(description = "Specify a region.")
            @RequestParam(required = false) String region,
            @Parameter(description = "Specify page.")
            @RequestParam(required = false) String page,
            @Parameter(description = "Specify size.")
            @RequestParam(required = false) String size,
            @Parameter(description = "Select property name for sorting.", schema = @Schema(
                    allowableValues = {"pharmacy.name", "pharmacy.name,DESC",
                            "pharmacy.address", "pharmacy.address,DESC",
                            "pharmacy.region", "pharmacy.region,DESC",
                            "workingHour.workingHourText", "workingHour.workingHourText,DESC"}))
            @RequestParam(required = false) String sort,
            @Parameter(hidden = true) @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(availablePharmacyService.getAvailablePharmacies(region, DateUtils.stringDateToInstant(date), pageable));
    }
}
