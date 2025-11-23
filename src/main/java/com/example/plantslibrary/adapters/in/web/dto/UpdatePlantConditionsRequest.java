package com.example.plantslibrary.adapters.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdatePlantConditionsRequest {

    @Min(0) @Max(100)
    private Integer hydrationLevel;

    @Min(0) @Max(100)
    private Integer humidityLevel;

    @Min(-10) @Max(50)
    private Double currentTemperature;

    private Boolean fertilizerNeeded;

    public Integer getHydrationLevel() {
        return hydrationLevel;
    }

    public void setHydrationLevel(Integer hydrationLevel) {
        this.hydrationLevel = hydrationLevel;
    }

    public Integer getHumidityLevel() {
        return humidityLevel;
    }

    public void setHumidityLevel(Integer humidityLevel) {
        this.humidityLevel = humidityLevel;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Boolean getFertilizerNeeded() {
        return fertilizerNeeded;
    }

    public void setFertilizerNeeded(Boolean fertilizerNeeded) {
        this.fertilizerNeeded = fertilizerNeeded;
    }
}
