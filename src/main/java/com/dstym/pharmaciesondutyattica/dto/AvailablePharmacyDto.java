package com.dstym.pharmaciesondutyattica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.Instant;

@Schema(description = "Available Pharmacy Information")
public record AvailablePharmacyDto(Long id, PharmacyDto pharmacy, WorkingHourDto workingHour, Instant date,
                                   Integer pulledVersion) implements Serializable {
}
