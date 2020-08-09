package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;

import java.util.List;

public interface WorkingHourService {
    List<WorkingHour> findAll();

    WorkingHour findById(String theId);

    void save(WorkingHour workingHour);

    void deleteById(String theId);
}
