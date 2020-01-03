package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;

import java.util.List;

public interface WorkingHourService {
    List<WorkingHour> findAll();

    WorkingHour findById(int theId);

    void save(WorkingHour workingHour);

    void deleteById(int theId);
}
