package com.dstym.pharmaciesondutyattica.repository.specification;

import com.dstym.pharmaciesondutyattica.model.AvailablePharmacy;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class AvailablePharmacySpecification {
    public static Specification<AvailablePharmacy> hasPulledVersion(Integer pulledVersion) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("pulledVersion"), pulledVersion);
    }

    public static Specification<AvailablePharmacy> hasDate(Instant date) {
        return (root, criteriaQuery, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.equal(root.get("date"), date);
    }

    public static Specification<AvailablePharmacy> hasRegion(String region) {
        return (root, criteriaQuery, criteriaBuilder) ->
                region == null ? null : criteriaBuilder.equal(root.get("pharmacy").get("region"), region);
    }
}
