package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;

import java.util.List;

public interface AvailablePharmacyService {
    List<AvailablePharmacy> findAll();

    List<AvailablePharmacy> findAllByRegionAndDate(String urlRegion, String urlDate);

    AvailablePharmacy findById(String theId);

    void save(AvailablePharmacy availablePharmacy);

    void deleteById(String theId);
}
