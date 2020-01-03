package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/get-data")
public class GetDataController {
    private WorkingHourRepository workingHourRepository;
    private PharmacyRepository pharmacyRepository;

    @Autowired
    public GetDataController(WorkingHourRepository workingHourRepository, PharmacyRepository pharmacyRepository) {
        this.workingHourRepository = workingHourRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    // commented to not be triggered second time
//    @GetMapping("/get-pharmacies-info")
//    public String getPharmacies() throws IOException {
//
//        var listOfPharmacies = PharmaciesInfo.getPharmaciesInfo();
//        for (var pharmacy : listOfPharmacies) {
//            pharmacyRepository.save(pharmacy);
//        }
//
//        return "Operation Completed!";
//    }

    // commented to not be triggered second time
//    @GetMapping("/get-working-hours-info")
//    public String getWorkingHours() throws IOException {
//
//        var listOfWorkingHours = WorkingHoursInfo.getWorkingHoursInfo();
//        for (var workingHour : listOfWorkingHours) {
//            // if id found, update
//            // else add
//            workingHourRepository.save(workingHour);
//        }
//
//        return "Operation Completed!";
//    }
}
