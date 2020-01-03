package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class WorkingHourServiceImpl implements WorkingHourService {
    private WorkingHourRepository workingHourRepository;

    @Autowired
    public WorkingHourServiceImpl(WorkingHourRepository workingHourRepository) {
        this.workingHourRepository = workingHourRepository;
    }

    @Override
    public List<WorkingHour> findAll() {
        return workingHourRepository.findAll();
    }

    @Override
    public WorkingHour findById(int theId) {
        Optional<WorkingHour> result = workingHourRepository.findById(theId);

        WorkingHour workingHour;

        if (result.isPresent()) {
            workingHour = result.get();
        } else {
            throw new RuntimeException("Did not find workingHour with id: " + theId);
        }

        return workingHour;
    }

    @Override
    public void save(WorkingHour workingHour) {
        workingHourRepository.save(workingHour);
    }

    @Override
    public void deleteById(int theId) {
        workingHourRepository.deleteById(theId);
    }
}
