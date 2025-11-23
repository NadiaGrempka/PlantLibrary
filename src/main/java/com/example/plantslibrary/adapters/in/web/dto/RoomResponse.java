package com.example.plantslibrary.adapters.in.web.dto;

public class RoomResponse {

    private String id;
    private String name;
    private Double targetTemperature;
    private Integer targetHumidity;
    private String windowOrientation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
