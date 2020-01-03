package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.Pharmacy;

import java.util.List;

public interface PharmacyService {
    List<Pharmacy> findAll();

    Pharmacy findById(int theId);

    void save(Pharmacy pharmacy);

    void deleteById(int theId);
}
