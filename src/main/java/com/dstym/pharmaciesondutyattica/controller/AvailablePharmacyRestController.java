package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AvailablePharmacyRestController {
    private AvailablePharmacyService availablePharmacyService;

    @Autowired
    public AvailablePharmacyRestController(AvailablePharmacyService availablePharmacyService) {
        this.availablePharmacyService = availablePharmacyService;
    }

    @GetMapping("/available-pharmacies")
    @ApiOperation(value = "Get Pharmacies On Duty",
            notes = "Get the List of the Pharmacies on Duty. Today is the default date.")
    public List<AvailablePharmacy> GetAvailablePharmacies(@RequestParam(required = false) String date,
                                                          @RequestParam(required = false) String region) {
        if (date == null || date.equals("")) {
            date = "today";
        }

        if (region == null || region.equals("")) {
            region = "all";
        }

        return availablePharmacyService.findAllByRegionAndDate(region, date);
    }
}
