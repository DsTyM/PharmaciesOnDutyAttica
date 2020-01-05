package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class AvailablePharmacyServiceImpl implements AvailablePharmacyService {
    private AvailablePharmacyRepository availablePharmacyRepository;
    private PharmacyRepository pharmacyRepository;

    @Autowired
    public AvailablePharmacyServiceImpl(AvailablePharmacyRepository availablePharmacyRepository,
                                        PharmacyRepository pharmacyRepository) {
        this.availablePharmacyRepository = availablePharmacyRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<AvailablePharmacy> findAll() {
        return availablePharmacyRepository.findAll();
    }

    @Override
    public List<AvailablePharmacy> findAllByRegionAndDate(String urlRegion, String urlDate) {
        var date = urlDate.replaceAll("-", "/");

        String region = URLDecoder.decode(urlRegion, StandardCharsets.UTF_8);

        if (date.equals("today")) {
            var daysFromToday = 0;
            date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        }

        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        if (result.isEmpty()) {
            throw new RuntimeException("Could not find pharmacies for the given date!");
        }

        var tempAvailablePharmacy = (AvailablePharmacy) result.toArray()[0];
        var lastPulledVersion = tempAvailablePharmacy.getPulledVersion();

        if (region.equals("all")) {
            return availablePharmacyRepository.findByDateAndAndPulledVersion(date, lastPulledVersion);
        }

        return availablePharmacyRepository.findByDateAndAndPulledVersionAndPharmacyRegion(date, lastPulledVersion, region);
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
