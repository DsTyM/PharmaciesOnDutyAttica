package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/get-data")
public class GetDataController {
    private WorkingHourRepository workingHourRepository;

    @Autowired
    public GetDataController(WorkingHourRepository workingHourRepository) {
        this.workingHourRepository = workingHourRepository;
    }

    @GetMapping("/get-working-hours")
    public String getWorkingHours() {

        WorkingHour workingHour = new WorkingHour(12345, "this is a test");

        workingHourRepository.save(workingHour);

        return "Operation Completed!";
    }
}
