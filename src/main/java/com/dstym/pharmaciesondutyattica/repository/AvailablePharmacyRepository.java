package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.model.AvailablePharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.Optional;

public interface AvailablePharmacyRepository extends JpaRepository<AvailablePharmacy, Long>, JpaSpecificationExecutor<AvailablePharmacy> {
    Optional<AvailablePharmacy> findFirstByDateOrderByPulledVersionDesc(Instant date);
}
