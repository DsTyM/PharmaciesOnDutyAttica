package com.dstym.pharmaciesondutyattica.mapper;

import com.dstym.pharmaciesondutyattica.dto.WorkingHourDto;
import com.dstym.pharmaciesondutyattica.model.WorkingHour;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkingHourMapper {
    WorkingHour getworkingHour(WorkingHourDto workingHourDto);

    WorkingHourDto getworkingHourDto(WorkingHour workingHour);
}
