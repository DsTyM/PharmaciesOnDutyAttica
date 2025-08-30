package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.model.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Integer> {
    List<WorkingHour> findAllByWorkingHourTextIn(List<String> workingHourTexts);
}
