package com.dstym.pharmaciesondutyattica.mapper;

import com.dstym.pharmaciesondutyattica.dto.PharmacyDto;
import com.dstym.pharmaciesondutyattica.model.Pharmacy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {
    Pharmacy getPharmacy(PharmacyDto pharmacyDto);

    PharmacyDto getPharmacyDto(Pharmacy pharmacy);
}
