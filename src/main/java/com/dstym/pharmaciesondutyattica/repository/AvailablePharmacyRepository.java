package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailablePharmacyRepository extends JpaRepository<AvailablePharmacy, Long> {
    List<AvailablePharmacy> findFirstByDateOrderByPulledVersionDesc(@Param("date") String date);
}
