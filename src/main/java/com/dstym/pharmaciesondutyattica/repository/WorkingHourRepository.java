package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Integer> {
    @Query(value = "select wh from WorkingHour wh")
    Page<WorkingHour> findAll(@Nullable Pageable pageable);

    List<WorkingHour> findFirstByWorkingHourText(String workingHourText);
}
