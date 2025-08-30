package com.dstym.pharmaciesondutyattica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Working Hour Information")
public record WorkingHourDto(Integer id, String workingHourText) implements Serializable {
}
