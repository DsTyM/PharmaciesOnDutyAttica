package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvailablePharmacyRepository extends MongoRepository<AvailablePharmacy, String> {
    List<AvailablePharmacy> findFirstByDateOrderByPulledVersionDesc(String date);

    List<AvailablePharmacy> findByDateAndAndPulledVersion(String date, int pulledVersion);

    List<AvailablePharmacy> findByDateAndAndPulledVersionAndPharmacyRegion(String date, int pulledVersion, String region);
}
