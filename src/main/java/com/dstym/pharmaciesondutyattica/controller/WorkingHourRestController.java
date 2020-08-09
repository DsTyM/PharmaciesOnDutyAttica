package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.assembler.WorkingHourAssembler;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
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
public class WorkingHourRestController {
    private final WorkingHourService workingHourService;
    private final WorkingHourAssembler workingHourAssembler;
    private final PagedResourcesAssembler<WorkingHour> pagedResourcesAssembler;

    @Autowired
    public WorkingHourRestController(WorkingHourService workingHourService,
                                     WorkingHourAssembler workingHourAssembler,
                                     PagedResourcesAssembler<WorkingHour> pagedResourcesAssembler) {
        this.workingHourService = workingHourService;
        this.workingHourAssembler = workingHourAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/working-hours")
    @Operation(summary = "Get Working Hours",
            description = "Get the List of the available Working hours a Pharmacy can have.")
    public ResponseEntity<PagedModel<WorkingHour>> getWorkingHours(
            @Parameter(hidden = true) @PageableDefault(value = 50) Pageable pageable) {
        Page<WorkingHour> workingHours = workingHourService.findAll(pageable);

        PagedModel<WorkingHour> workingHourModel = pagedResourcesAssembler
                .toModel(workingHours, workingHourAssembler);

        return new ResponseEntity<>(workingHourModel, HttpStatus.OK);
    }

    @GetMapping("/working-hours/{workingHourId}")
    @Operation(summary = "Find a Working Hour by Id",
            description = "Returns a specific Working Hour info by the given Id.")
    public ResponseEntity<WorkingHour> getWorkingHour(
            @Parameter(description = "Specify the Working Hour ID.") @PathVariable int workingHourId) {
        return workingHourService.findById(workingHourId)
                .map(workingHourAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
