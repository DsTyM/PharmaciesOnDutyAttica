package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkingHourServiceImpl implements WorkingHourService {
    private final WorkingHourRepository workingHourRepository;

    @Autowired
    public WorkingHourServiceImpl(WorkingHourRepository workingHourRepository) {
        this.workingHourRepository = workingHourRepository;
    }

    @Override
    @Cacheable(value = "workingHoursPageableCache", key = "#pageable")
    public Page<WorkingHour> findAll(Pageable pageable) {
        return workingHourRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "workingHoursCache", key = "'ALL'")
    public List<WorkingHour> findAll() {
        return (List<WorkingHour>) workingHourRepository.findAll();
    }

    @Override
    @Cacheable(value = "workingHourCache", key = "#theId")
    public Optional<WorkingHour> findById(int theId) {
        Optional<WorkingHour> result = workingHourRepository.findById(theId);

        if (result.isPresent()) {
            return result;
        } else {
            throw new RuntimeException("Did not find workingHour with id: " + theId);
        }
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
