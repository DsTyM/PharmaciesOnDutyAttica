package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WorkingHourService {
    private final WorkingHourRepository workingHourRepository;

    //    @Cacheable(value = "workingHoursCache", key = "'ALL'")
    @Cacheable(value = "workingHoursCache", key = "#pageable", condition = "#pageable != null")
    public Page<WorkingHour> findAll(Pageable pageable) {
        Page<WorkingHour> result = workingHourRepository.findAll(pageable);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find working hours.");
        } else {
            return result;
        }
    }

    @Cacheable(value = "workingHourCache", key = "#theId")
    public WorkingHour findById(Integer theId) {
        var result = workingHourRepository.findById(theId);

        WorkingHour workingHour;

        if (result.isPresent()) {
            workingHour = result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find working hour with id: " + theId);
        }

        return workingHour;
    }

    public void save(WorkingHour workingHour) {
        workingHourRepository.save(workingHour);
    }

    public void deleteById(Integer theId) {
        workingHourRepository.deleteById(theId);
    }
}
