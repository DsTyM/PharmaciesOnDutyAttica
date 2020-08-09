package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class PharmacyServiceImpl implements PharmacyService {
    private PharmacyRepository pharmacyRepository;

    @Autowired
    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Pharmacy findById(String theId) {
        Optional<Pharmacy> result = pharmacyRepository.findById(theId);

        Pharmacy pharmacy;

        if (result.isPresent()) {
            pharmacy = result.get();
        } else {
            throw new RuntimeException("Did not find pharmacy with id: " + theId);
        }

        return pharmacy;
    }

    @Override
    public List<Pharmacy> findByRegion(String urlRegion) {
        String region = URLDecoder.decode(urlRegion, StandardCharsets.UTF_8);

        List<Pharmacy> result = pharmacyRepository.findByRegion(region);

        if (result.isEmpty()) {
            throw new RuntimeException("Did not find pharmacy in region: " + region);
        }

        return result;
    }

    @Override
    public void save(Pharmacy pharmacy) {
        pharmacyRepository.save(pharmacy);
    }

    @Override
    public void deleteById(String theId) {
        pharmacyRepository.deleteById(theId);
    }
}
