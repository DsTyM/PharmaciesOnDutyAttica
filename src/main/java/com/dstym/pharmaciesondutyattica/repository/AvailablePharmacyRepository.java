package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailablePharmacyRepository extends JpaRepository<AvailablePharmacy, Long> {
    @Query(value = "SELECT * FROM `available-pharmacies` ap " +
            "where ap.date=:date order by ap.`pulled-version` desc limit 1",
            nativeQuery = true)
    List<AvailablePharmacy> getLastPulledVersion(@Param("date") String date);
}
