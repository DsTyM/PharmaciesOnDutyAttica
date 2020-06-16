package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class WorkingHourRestController {
    private WorkingHourService workingHourService;

    @Autowired
    public WorkingHourRestController(WorkingHourService workingHourService) {
        this.workingHourService = workingHourService;
    }

    @GetMapping("/working-hours")
    @Operation(summary = "Get Working Hours",
            description = "Get the List of the available Working hours a Pharmacy can have.")
    public List<WorkingHour> getWorkingHours() {
        return workingHourService.findAll();
    }

    @GetMapping("/working-hours/{workingHourId}")
    @Operation(summary = "Find a Working Hour by Id",
            description = "Returns a specific Working Hour info by the given Id.")
    public WorkingHour getWorkingHour(
            @Parameter(description = "Specify the Working Hour ID.")
            @PathVariable int workingHourId) {
        return workingHourService.findById(workingHourId);
    }
}
