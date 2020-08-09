package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private static int getLastPulledVersion(String date, Pageable pageable) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(
                date, pageable);

        if (result.isEmpty()) {
            throw new RuntimeException("Did not find available pharmacies for date: " + date);
        }

        var tempAvailablePharmacy = (AvailablePharmacy) result.get().toArray()[0];
        return tempAvailablePharmacy.getPulledVersion();
    }

    @Override
    public List<AvailablePharmacy> findAll() {
        return (List<AvailablePharmacy>) availablePharmacyRepository.findAll();
    }

    @Override
    @Cacheable(value = "availablePharmaciesPageableCache",
            key = "#urlRegion + '_' + #urlDate + '_' + #pageable.hashCode()")
    public Page<AvailablePharmacy> findAllByRegionAndDate(String urlRegion, String urlDate, Pageable pageable) {
        var date = urlDate.replaceAll("-", "/");

        var region = URLDecoder.decode(urlRegion, StandardCharsets.UTF_8);

        if (date.equals("today")) {
            var daysFromToday = 0;
            date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        }

        var lastPulledVersion = getLastPulledVersion(date, pageable);

        if (region.equals("all")) {
            return availablePharmacyRepository.findByDateAndAndPulledVersion(date, lastPulledVersion, pageable);
        }

        var result = availablePharmacyRepository.findByDateAndAndPulledVersionAndPharmacyRegion(
                date, lastPulledVersion, region, pageable);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new RuntimeException("Did not find available pharmacies in region: " + region);
        }
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
