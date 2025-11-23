package com.example.plantslibrary.adapters.in.web.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlantCreateRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Min(0) @Max(100)
    private int hydrationLevel;

    @Min(0) @Max(100)
    private int humidityLevel;

    @NotNull
    private String sunlightLevel;

    private boolean fertilizerNeeded;

    @Min(-10) @Max(50)
    private double currentTemperature;

    private String roomId;
}
