package com.dstym.pharmaciesondutyattica.mapper;

import com.dstym.pharmaciesondutyattica.dto.AvailablePharmacyDto;
import com.dstym.pharmaciesondutyattica.model.AvailablePharmacy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvailablePharmacyMapper {
    AvailablePharmacy getAvailablePharmacy(AvailablePharmacyDto availablePharmacyDto);

    AvailablePharmacyDto getAvailablePharmacyDto(AvailablePharmacy availablePharmacy);
}
