package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PharmacyRestController {
    private PharmacyService pharmacyService;

    @Autowired
    public PharmacyRestController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/pharmacies")
    @ResponseBody
    public List<Pharmacy> findAll(@RequestParam(required = false) String region) {
        if (region != null) {
            return pharmacyService.findByRegion(region);
        }

        return pharmacyService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    public Pharmacy getPharmacy(@PathVariable int pharmacyId) {

        return pharmacyService.findById(pharmacyId);
    }
}
