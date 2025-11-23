package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;

public class CreatePlantUseCase implements CreatePlantUsePort {

    private final PlantRepositoryPort plantRepositoryPort;

    public CreatePlantUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }

    @Override
    public Plant createPlant(CreatePlantCommand command) {
        Plant plant = new Plant();

        plant.setName(command.name());
        plant.setHydrationLevel(command.hydrationLevel());
        plant.setHumidityLevel(command.humidityLevel());
        plant.setSunlightLevel(SunlightLevel.valueOf(command.sunlightLevel().toUpperCase()));
        plant.setFertilizerNeeded(command.fertilizerNeeded());
        plant.setCurrentTemperature(command.currentTemperature());
        plant.setRoomId(command.roomId());
        plant.setTemperatureComfort(evaluateTemperatureComfort(command.currentTemperature()));

        return plantRepositoryPort.save(plant);
    }

    private TemperatureComfort evaluateTemperatureComfort(double temperature) {
        if (temperature < 15.0) {
            return TemperatureComfort.TOO_COLD;
        } else if (temperature > 28.0) {
            return TemperatureComfort.TOO_HOT;
        } else {
            return TemperatureComfort.OK;
        }
    }
}
