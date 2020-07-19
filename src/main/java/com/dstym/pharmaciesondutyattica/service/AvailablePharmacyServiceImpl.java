package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<AvailablePharmacy> findAll() {
        return availablePharmacyRepository.findAll();
    }

    @Override
    public List<AvailablePharmacy> findAllByRegionAndDate(String urlRegion, String urlDate) {
        var date = urlDate.replaceAll("-", "/");

        var region = URLDecoder.decode(urlRegion, StandardCharsets.UTF_8);

        if (date.equals("today")) {
            var daysFromToday = 0;
            date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        }

        var lastPulledVersion = getLastPulledVersion(date);

        if (region.equals("all")) {
            return availablePharmacyRepository.findByDateAndAndPulledVersion(date, lastPulledVersion);
        }

        var result = availablePharmacyRepository.findByDateAndAndPulledVersionAndPharmacyRegion(date,
                lastPulledVersion, region);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new RuntimeException("Did not find available pharmacies in region: " + region);
        }
    }

    private static int getLastPulledVersion(String date) {
        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        if (result.isEmpty()) {
            throw new RuntimeException("Did not find available pharmacies for date: " + date);
        }

        var tempAvailablePharmacy = (AvailablePharmacy) result.toArray()[0];
        return tempAvailablePharmacy.getPulledVersion();
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
