package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer>, JpaSpecificationExecutor<Pharmacy> {
    List<Pharmacy> findAllByNameIn(List<String> names);
}
