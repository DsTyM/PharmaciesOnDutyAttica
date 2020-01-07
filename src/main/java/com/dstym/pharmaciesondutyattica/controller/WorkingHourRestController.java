package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
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
    public List<WorkingHour> findAll() {
        return workingHourService.findAll();
    }

    @GetMapping("/working-hours/{workingHourId}")
    public WorkingHour getEmployee(@PathVariable int workingHourId) {

        return workingHourService.findById(workingHourId);
    }
}
