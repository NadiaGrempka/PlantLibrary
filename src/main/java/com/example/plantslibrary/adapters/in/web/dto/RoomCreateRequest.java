package com.example.plantslibrary.adapters.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoomCreateRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Min(-10) @Max(40)
    private Double targetTemperature;

    @Min(0) @Max(100)
    private Integer targetHumidity;

    @NotBlank
    private String windowOrientation; // np. N, S, E, W

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public Integer getTargetHumidity() {
        return targetHumidity;
    }

    public void setTargetHumidity(Integer targetHumidity) {
        this.targetHumidity = targetHumidity;
    }

    public String getWindowOrientation() {
        return windowOrientation;
    }

    public void setWindowOrientation(String windowOrientation) {
        this.windowOrientation = windowOrientation;
    }
}
