package com.example.plantslibrary.adapters.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlantResponse {
    private String id;
    private String name;
    private int hydrationLevel;
    private int humidityLevel;
    private String sunlightLevel;
    private boolean fertilizerNeeded;
    private double currentTemperature;
    private String temperatureComfort;
    private String roomId;
}
