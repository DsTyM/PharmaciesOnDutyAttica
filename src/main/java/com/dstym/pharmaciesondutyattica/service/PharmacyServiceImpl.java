package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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
    public Pharmacy findById(int theId) {
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
    public void save(Pharmacy pharmacy) {
        pharmacyRepository.save(pharmacy);
    }

    @Override
    public void deleteById(int theId) {
        pharmacyRepository.deleteById(theId);
    }
}
