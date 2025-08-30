package com.dstym.pharmaciesondutyattica.service;

import com.dstym.pharmaciesondutyattica.dto.WorkingHourDto;
import com.dstym.pharmaciesondutyattica.mapper.WorkingHourMapper;
import com.dstym.pharmaciesondutyattica.model.WorkingHour;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkingHourService {
    private final WorkingHourRepository workingHourRepository;
    private final WorkingHourMapper workingHourMapper;

    //    @Cacheable(value = "workingHoursCache", key = "'ALL'")
    @Cacheable(value = "workingHoursCache", key = "#pageable", condition = "#pageable != null")
    public Page<WorkingHourDto> getWorkingHours(Pageable pageable) {
        return Optional.of(workingHourRepository.findAll(pageable).map(workingHourMapper::getworkingHourDto))
                .filter(Page::hasContent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find working hours."));
    }

    @Cacheable(value = "workingHourCache", key = "#workingHourId")
    public WorkingHourDto getWorkingHour(Integer workingHourId) {
        return workingHourRepository.findById(workingHourId).map(workingHourMapper::getworkingHourDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Did not find working hour with id: " + workingHourId));
    }

    public WorkingHour createWorkingHour(WorkingHour workingHour) {
        return workingHourRepository.save(workingHour);
    }

    public void deleteWorkingHour(Integer workingHourId) {
        workingHourRepository.deleteById(workingHourId);
    }
}
