package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.dto.PharmacyDto;
import com.dstym.pharmaciesondutyattica.mapper.PharmacyMapper;
import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PharmacyService {
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;

    @Cacheable(value = "pharmacyCache", key = "#pharmacyId")
    public PharmacyDto findById(Integer pharmacyId) {
        return pharmacyRepository.findById(pharmacyId).map(pharmacyMapper::getPharmacyDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Did not find pharmacy with id: " + pharmacyId));
    }

    @Cacheable(value = "pharmaciesCache", key = "{#region, #pageable}")
    public Page<PharmacyDto> findAll(String region, Pageable pageable) {
        region = Optional.ofNullable(region)
                .map(r -> URLDecoder.decode(r.trim(), StandardCharsets.UTF_8))
                .orElse(null);

        return Optional.of(pharmacyRepository.findAll(region, pageable).map(pharmacyMapper::getPharmacyDto))
                .filter(Page::hasContent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find pharmacies."));
    }

    public Pharmacy save(Pharmacy pharmacy) {
        return pharmacyRepository.save(pharmacy);
    }

    public void deleteById(Integer pharmacyId) {
        pharmacyRepository.deleteById(pharmacyId);
    }
}
