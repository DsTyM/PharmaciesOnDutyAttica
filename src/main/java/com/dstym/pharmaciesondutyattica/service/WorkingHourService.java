package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkingHourService {
    Page<WorkingHour> findAll(Pageable pageable);

    WorkingHour findById(int theId);

    void save(WorkingHour workingHour);

    void deleteById(int theId);
}
