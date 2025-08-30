package com.dstym.pharmaciesondutyattica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Pharmacy Information")
public record PharmacyDto(Integer id, String name, String address, String region,
                          String phoneNumber) implements Serializable {
}
