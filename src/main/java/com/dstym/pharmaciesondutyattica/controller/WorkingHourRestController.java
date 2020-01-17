package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkingHourRestController {
    private WorkingHourService workingHourService;

    @Autowired
    public WorkingHourRestController(WorkingHourService workingHourService) {
        this.workingHourService = workingHourService;
    }

    @GetMapping("/working-hours")
    @ApiOperation(value = "Get Working Hours",
            notes = "Get the List of the available Working hours a Pharmacy can have.")
    public List<WorkingHour> getWorkingHours() {
        return workingHourService.findAll();
    }

    @GetMapping("/working-hours/{workingHourId}")
    @ApiOperation(value = "Find a Working Hour by Id",
            notes = "Returns a specific Working Hour info by the given Id.")
    public WorkingHour getWorkingHour(@PathVariable int workingHourId) {

        return workingHourService.findById(workingHourId);
    }
}
