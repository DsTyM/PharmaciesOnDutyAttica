package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class AvailablePharmacyServiceImpl implements AvailablePharmacyService {
    private static AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    public AvailablePharmacyServiceImpl(AvailablePharmacyRepository availablePharmacyRepository) {
        AvailablePharmacyServiceImpl.availablePharmacyRepository = availablePharmacyRepository;
    }

    private static int getLastPulledVersion(String date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Did not find available pharmacies for date: " + date);
        }

        var tempAvailablePharmacy = result.get(0);
        return tempAvailablePharmacy.getPulledVersion();
    }

    @Override
    public List<AvailablePharmacy> findAll() {
        return availablePharmacyRepository.findAll();
    }

    @Override
    @Cacheable(value = "availablePharmaciesCache", key = "{#region, #date, #pageable}")
    public Page<AvailablePharmacy> findAllByRegionAndDate(String region, String date, Pageable pageable) {
        region = Optional.ofNullable(region)
                .map(r -> URLDecoder.decode(r.trim(), StandardCharsets.UTF_8))
                .orElse(null);

        var daysFromToday = 0;
        date = Optional.ofNullable(date)
                .map(d -> d.replaceAll("-", "/"))
                .orElse(DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday)));

        var lastPulledVersion = getLastPulledVersion(date);

        var result = availablePharmacyRepository.findAllByLastPulledVersion(
                lastPulledVersion, date, region, pageable);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Did not find available pharmacies.");
        }
    }

    @Override
    public AvailablePharmacy findById(long theId) {
        Optional<AvailablePharmacy> result = availablePharmacyRepository.findById(theId);

        AvailablePharmacy availablePharmacy;

        if (result.isPresent()) {
            availablePharmacy = result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Did not find availablePharmacy with id: " + theId);
        }

        return availablePharmacy;
    }

    public void save(AvailablePharmacy availablePharmacy) {
        availablePharmacyRepository.save(availablePharmacy);
    }

    public void deleteById(long theId) {
        availablePharmacyRepository.deleteById(theId);
    }
}
