package com.example.plantslibrary.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Plant {

    private String id;
    private String name;
    // poziom nawodnienia 0–100
    private int hydrationLevel;
    // poziom wilgotności 0–100
    private int humidityLevel;
    private SunlightLevel sunlightLevel;
    private boolean fertilizerNeeded;
    private double currentTemperature;
    private TemperatureComfort temperatureComfort;
    private String roomId;

}
