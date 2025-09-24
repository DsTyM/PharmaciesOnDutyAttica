package com.dstym.pharmaciesondutyattica.repository.specification;

import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import org.springframework.data.jpa.domain.Specification;

public class PharmacySpecification {
    public static Specification<Pharmacy> hasRegion(String region) {
        return (root, criteriaQuery, criteriaBuilder) ->
                region == null ? null : criteriaBuilder.equal(root.get("region"), region);
    }
}
