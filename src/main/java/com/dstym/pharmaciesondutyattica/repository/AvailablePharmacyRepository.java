package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AvailablePharmacyRepository extends PagingAndSortingRepository<AvailablePharmacy, Long> {
    Page<AvailablePharmacy> findFirstByDateOrderByPulledVersionDesc(String date, Pageable pageable);

    Page<AvailablePharmacy> findByDateAndAndPulledVersion(String date, int pulledVersion, Pageable pageable);

    Page<AvailablePharmacy> findByDateAndAndPulledVersionAndPharmacyRegion(
            String date, int pulledVersion, String region, Pageable pageable);
}
