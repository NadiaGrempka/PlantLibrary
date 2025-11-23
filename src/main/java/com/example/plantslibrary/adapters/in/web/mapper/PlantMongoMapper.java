package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;

public class PlantMongoMapper {
    public static Plant toDomain(PlantDocument doc) {
        Plant plant = new Plant();
        plant.setId(doc.getId());
        plant.setName(doc.getName());
        plant.setHydrationLevel(doc.getHydrationLevel());
        plant.setHumidityLevel(doc.getHumidityLevel());
        plant.setSunlightLevel(SunlightLevel.valueOf(doc.getSunlightLevel()));
        plant.setFertilizerNeeded(doc.getFertilizerNeeded());
        plant.setCurrentTemperature(doc.getCurrentTemperature());
        plant.setTemperatureComfort(TemperatureComfort.valueOf(doc.getTemperatureComfort()));
        plant.setRoomId(doc.getRoomId());
        return plant;
    }
}

