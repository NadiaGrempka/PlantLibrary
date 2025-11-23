package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.in.web.dto.PlantCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.PlantResponse;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;

public class PlantWebMapper {
    public static CreatePlantUsePort.CreatePlantCommand toCommand(PlantCreateRequest dto) {
        return new CreatePlantUsePort.CreatePlantCommand(
                dto.getName(),
                dto.getHydrationLevel(),
                dto.getHumidityLevel(),
                dto.getSunlightLevel(),
                dto.isFertilizerNeeded(),
                dto.getCurrentTemperature(),
                dto.getRoomId()
        );
    }

    public static PlantResponse toResponse(Plant plant) {
        PlantResponse resp = new PlantResponse();
        resp.setId(plant.getId());
        resp.setName(plant.getName());
        resp.setHydrationLevel(plant.getHydrationLevel());
        resp.setHumidityLevel(plant.getHumidityLevel());
        resp.setSunlightLevel(plant.getSunlightLevel().name());
        resp.setFertilizerNeeded(plant.isFertilizerNeeded());
        resp.setCurrentTemperature(plant.getCurrentTemperature());
        resp.setTemperatureComfort(plant.getTemperatureComfort().name());
        resp.setRoomId(plant.getRoomId());
        return resp;

//        TemperatureComfort comfort = plant.getTemperatureComfort();
//        resp.setTemperatureComfort(comfort != null ? comfort.name() : null);

//        resp.setRoomId(plant.getRoomId());
//        return resp;
    }

}
