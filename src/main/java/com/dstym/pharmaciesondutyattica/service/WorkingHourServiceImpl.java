package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkingHourServiceImpl implements WorkingHourService {
    private final WorkingHourRepository workingHourRepository;

    @Autowired
    public WorkingHourServiceImpl(WorkingHourRepository workingHourRepository) {
        this.workingHourRepository = workingHourRepository;
    }

    @Override
//    @Cacheable(value = "workingHoursCache", key = "'ALL'")
    @Cacheable(value = "workingHoursCache", key = "#pageable")
    public Page<WorkingHour> findAll(Pageable pageable) {
        return workingHourRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "workingHourCache", key = "#theId")
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
