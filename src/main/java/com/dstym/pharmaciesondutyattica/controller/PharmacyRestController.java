package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Pharmacy> findAll() {
        return pharmacyService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    public Pharmacy getEmployee(@PathVariable int pharmacyId) {

        Pharmacy pharmacy = pharmacyService.findById(pharmacyId);

        if (pharmacy == null) {
            throw new RuntimeException("Pharmacy with id not found: " + pharmacyId);
        }

        return pharmacy;
    }
}
