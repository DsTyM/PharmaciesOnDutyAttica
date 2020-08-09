package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PharmacyRepository extends PagingAndSortingRepository<Pharmacy, Integer> {
    Page<Pharmacy> findByRegion(String region, Pageable pageable);
}
