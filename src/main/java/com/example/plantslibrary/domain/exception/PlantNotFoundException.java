package com.example.plantslibrary.domain.exception;

public class PlantNotFoundException extends RuntimeException {

    private final String plantId;

    public PlantNotFoundException(String plantId) {
        super("Plant with id " + plantId + " not found");
        this.plantId = plantId;
    }

    public String getPlantId() {
        return plantId;
    }
}
