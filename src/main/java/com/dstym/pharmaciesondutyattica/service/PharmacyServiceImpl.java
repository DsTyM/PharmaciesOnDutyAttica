package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class PharmacyServiceImpl implements PharmacyService {
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    @Cacheable(value = "pharmacyCache", key = "#theId")
    public Pharmacy findById(int theId) {
        var result = pharmacyRepository.findById(theId);

        Pharmacy pharmacy;

        if (result.isPresent()) {
            pharmacy = result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find pharmacy with id: " + theId);
        }

        return pharmacy;
    }

    @Override
    @Cacheable(value = "pharmaciesCache", key = "{#region, #pageable}")
    public Page<Pharmacy> findAll(String region, Pageable pageable) {
        region = Optional.ofNullable(region)
                .map(r -> URLDecoder.decode(r.trim(), StandardCharsets.UTF_8))
                .orElse(null);

        var result = pharmacyRepository.findAll(region, pageable);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find pharmacies.");
        }

        return result;
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
