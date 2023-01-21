package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
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
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Working Hour")
public class WorkingHourRestController {
    private final WorkingHourService workingHourService;

    @GetMapping("/working-hours")
    @Operation(summary = "Get Working Hours",
            description = "Get the List of the available Working hours a Pharmacy can have.")
    public ResponseEntity<Page<WorkingHour>> getWorkingHours(
            @Parameter(description = "Specify page.")
            @RequestParam(required = false) String page,
            @Parameter(description = "Specify size.")
            @RequestParam(required = false) String size,
            @Parameter(description = "Select property name for sorting.", schema = @Schema(
                    allowableValues = {"workingHourText", "workingHourText,DESC"}))
            @RequestParam(required = false) String sort,
            @Parameter(hidden = true) @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(workingHourService.findAll(pageable));
    }

    @GetMapping("/working-hours/{workingHourId}")
    @Operation(summary = "Find a Working Hour by Id",
            description = "Returns a specific Working Hour info by the given Id.")
    public ResponseEntity<WorkingHour> getWorkingHour(
            @Parameter(description = "Specify the Working Hour ID.")
            @PathVariable Integer workingHourId) {
        return ResponseEntity.ok(workingHourService.findById(workingHourId));
    }
}
