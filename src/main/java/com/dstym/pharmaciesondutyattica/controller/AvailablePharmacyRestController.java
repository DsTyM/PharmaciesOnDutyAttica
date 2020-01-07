package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AvailablePharmacyRestController {
    private AvailablePharmacyService availablePharmacyService;

    @Autowired
    public AvailablePharmacyRestController(AvailablePharmacyService availablePharmacyService) {
        this.availablePharmacyService = availablePharmacyService;
    }

    @GetMapping("/available-pharmacies")
    public List<AvailablePharmacy> findAllByDate(@RequestParam(required = false) String date,
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
