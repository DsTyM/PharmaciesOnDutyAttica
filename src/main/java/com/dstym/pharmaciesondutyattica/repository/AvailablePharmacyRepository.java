package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface AvailablePharmacyRepository extends JpaRepository<AvailablePharmacy, Long> {
    List<AvailablePharmacy> findFirstByDateOrderByPulledVersionDesc(String date);

    @Query(value = "select ap from AvailablePharmacy ap " +
            "where ap.pulledVersion=:pulledVersion " +
            "and (ap.date=:date or :date is null) " +
            "and (ap.pharmacy.region=:region or :region is null)")
    Page<AvailablePharmacy> findAllByLastPulledVersion(@Param("pulledVersion") int pulledVersion,
                                                       @Param("date") String date,
                                                       @Param("region") String region,
                                                       @Nullable Pageable pageable);
}
