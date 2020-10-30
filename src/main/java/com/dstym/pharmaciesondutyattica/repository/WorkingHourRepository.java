package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Integer> {
    Page<WorkingHour> findAll(@Nullable Pageable pageable);
}
