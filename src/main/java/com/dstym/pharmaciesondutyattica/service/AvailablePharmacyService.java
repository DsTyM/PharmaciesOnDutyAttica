package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailablePharmacyService {
    private final AvailablePharmacyRepository availablePharmacyRepository;

    /**
     * Retrieves the latest pulled version for a given date.
     *
     * @param date the date for which to retrieve the latest pulled version.
     * @return the latest pulled version as an integer.
     * @throws ResponseStatusException if no available pharmacies are found for the given date.
     */
    private int getLastPulledVersion(Instant date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find available pharmacies for date: " + DateUtils.instantToString(date));
        }

        var tempAvailablePharmacy = result.get(0);
        return tempAvailablePharmacy.getPulledVersion();
    }

    public List<AvailablePharmacy> findAll() {
        return availablePharmacyRepository.findAll();
    }

    @Cacheable(value = "availablePharmaciesCache", key = "{#region, #date, #pageable}")
    public Page<AvailablePharmacy> findAllByRegionAndDate(String region, Instant date, Pageable pageable) {
        region = Optional.ofNullable(region)
                .map(r -> URLDecoder.decode(r.trim(), StandardCharsets.UTF_8))
                .orElse(null);

        var daysFromToday = 0;
        date = Optional.ofNullable(date)
                .orElse(DateUtils.stringDateToInstant(DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday))));

        var lastPulledVersion = getLastPulledVersion(date);

        var result = availablePharmacyRepository.findAllByLastPulledVersion(
                lastPulledVersion, date, region, pageable);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find available pharmacies for date: " + DateUtils.instantToString(date));
        }
    }

    public AvailablePharmacy findById(Long theId) {
        var result = availablePharmacyRepository.findById(theId);

        AvailablePharmacy availablePharmacy;

        if (result.isPresent()) {
            availablePharmacy = result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find available pharmacy with id: " + theId);
        }

        return availablePharmacy;
    }

    public void save(AvailablePharmacy availablePharmacy) {
        availablePharmacyRepository.save(availablePharmacy);
    }

    public void deleteById(Long theId) {
        availablePharmacyRepository.deleteById(theId);
    }
}
