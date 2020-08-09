package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PharmacyService {
    Page<Pharmacy> findAll(Pageable pageable);

    List<Pharmacy> findAll();

    Pharmacy findById(int theId);

    Page<Pharmacy> findByRegion(String urlRegion, Pageable pageable);

    void save(Pharmacy pharmacy);

    void deleteById(int theId);
}
