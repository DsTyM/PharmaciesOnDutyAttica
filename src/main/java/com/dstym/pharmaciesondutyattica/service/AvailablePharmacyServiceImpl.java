package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AvailablePharmacyServiceImpl implements AvailablePharmacyService {
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    public AvailablePharmacyServiceImpl(AvailablePharmacyRepository availablePharmacyRepository) {
        this.availablePharmacyRepository = availablePharmacyRepository;
    }

    @Override
    public List<AvailablePharmacy> findAll() {
        return availablePharmacyRepository.findAll();
    }

    @Override
    public AvailablePharmacy findById(long theId) {
        Optional<AvailablePharmacy> result = availablePharmacyRepository.findById(theId);

        AvailablePharmacy availablePharmacy;

        if (result.isPresent()) {
            availablePharmacy = result.get();
        } else {
            throw new RuntimeException("Did not find availablePharmacy with id: " + theId);
        }

        return availablePharmacy;
    }

    @Override
    public void save(AvailablePharmacy availablePharmacy) {
        availablePharmacyRepository.save(availablePharmacy);
    }

    @Override
    public void deleteById(long theId) {
        availablePharmacyRepository.deleteById(theId);
    }
}
