package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;

import java.util.List;

public interface PharmacyService {
    List<Pharmacy> findAll();

    Pharmacy findById(String theId);

    List<Pharmacy> findByRegion(String urlRegion);

    void save(Pharmacy pharmacy);

    void deleteById(String theId);
}
