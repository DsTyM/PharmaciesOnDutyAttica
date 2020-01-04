package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/available-pharmacies-today")
    public List<AvailablePharmacy> findAllToday() {
        return availablePharmacyService.findAllToday();
    }

    @GetMapping("/available-pharmacies/{date}")
    public List<AvailablePharmacy> findAllByDate(@PathVariable String date) {
        return availablePharmacyService.findAllByDate(date);
    }
}
