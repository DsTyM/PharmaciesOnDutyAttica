package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkingHourService {
    Page<WorkingHour> findAll(Pageable pageable);

    List<WorkingHour> findAll();

    Optional<WorkingHour> findById(int theId);

    void save(WorkingHour workingHour);

    void deleteById(int theId);
}
