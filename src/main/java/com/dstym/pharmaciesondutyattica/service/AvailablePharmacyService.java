package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface AvailablePharmacyService {
    List<AvailablePharmacy> findAll();

    Page<AvailablePharmacy> findAllByRegionAndDate(String urlRegion, Instant urlDate, Pageable pageable);

    AvailablePharmacy findById(long theId);

    void save(AvailablePharmacy availablePharmacy);

    void deleteById(long theId);
}
