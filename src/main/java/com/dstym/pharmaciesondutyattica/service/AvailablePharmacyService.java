package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.dto.AvailablePharmacyDto;
import com.dstym.pharmaciesondutyattica.mapper.AvailablePharmacyMapper;
import com.dstym.pharmaciesondutyattica.model.AvailablePharmacy;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailablePharmacyService {
    private final AvailablePharmacyRepository availablePharmacyRepository;
    private final AvailablePharmacyMapper availablePharmacyMapper;

    /**
     * Retrieves the latest pulled version for a given date.
     *
     * @param date the date for which to retrieve the latest pulled version.
     * @return the latest pulled version as an integer.
     * @throws ResponseStatusException if no available pharmacies are found for the given date.
     */
    private int getLastPulledVersion(Instant date) {
        return availablePharmacyRepository
                .findFirstByDateOrderByPulledVersionDesc(date).map(AvailablePharmacy::getPulledVersion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Did not find available pharmacies for date: " + DateUtils.instantToString(date)));
    }

    /**
     * Retrieves a paginated list of available pharmacies within a specific region and date.
     *
     * @param region   the region for which to find available pharmacies. Can be null or URL-encoded.
     * @param date     the specific date to find available pharmacies for. If null, defaults to the current date.
     * @param pageable the pagination and sorting information.
     * @return a page of {@link AvailablePharmacyDto} containing the details of available pharmacies.
     * @throws ResponseStatusException if no pharmacies are found for the specified region and date.
     */
    @Cacheable(value = "availablePharmaciesCache", key = "{#region, #date, #pageable}")
    public Page<AvailablePharmacyDto> findAllByRegionAndDate(String region, Instant date, Pageable pageable) {
        region = Optional.ofNullable(region)
                .map(r -> URLDecoder.decode(r.trim(), StandardCharsets.UTF_8))
                .orElse(null);

        var daysFromToday = 0;
        var finalDate = Optional.ofNullable(date)
                .orElse(DateUtils.stringDateToInstant(DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday))));

        var lastPulledVersion = getLastPulledVersion(finalDate);

        return Optional.of(availablePharmacyRepository.findAllByLastPulledVersion(lastPulledVersion, finalDate, region, pageable)
                        .map(availablePharmacyMapper::getAvailablePharmacyDto))
                .filter(Page::hasContent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Did not find available pharmacies for date: " + DateUtils.instantToString(finalDate)));
    }

    public AvailablePharmacyDto findById(Long availablePharmacyId) {
        return availablePharmacyRepository.findById(availablePharmacyId)
                .map(availablePharmacyMapper::getAvailablePharmacyDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Did not find pharmacy with id: " + availablePharmacyId));
    }

    public AvailablePharmacy save(AvailablePharmacy availablePharmacy) {
        return availablePharmacyRepository.save(availablePharmacy);
    }

    public void deleteById(Long availablePharmacyId) {
        availablePharmacyRepository.deleteById(availablePharmacyId);
    }
}
