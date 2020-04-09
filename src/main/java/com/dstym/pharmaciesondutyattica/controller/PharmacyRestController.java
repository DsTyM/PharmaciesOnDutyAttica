package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PharmacyRestController {
    private PharmacyService pharmacyService;

    @Autowired
    public PharmacyRestController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/pharmacies")
    @ResponseBody
    @ApiOperation(value = "Get Pharmacies",
            notes = "Get the List of the Pharmacies in the Database.")
    public List<Pharmacy> getPharmacies(
            @ApiParam("Specify a region.")
            @RequestParam(required = false) String region) {
        if (region != null) {
            return pharmacyService.findByRegion(region);
        }

        return pharmacyService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}")
    @ApiOperation(value = "Find a Pharmacy by Id",
            notes = "Returns a specific Pharmacy info by the given Id.")
    public Pharmacy getPharmacy(
            @ApiParam("Specify the Pharmacy ID.")
            @PathVariable int pharmacyId) {
        return pharmacyService.findById(pharmacyId);
    }
}
