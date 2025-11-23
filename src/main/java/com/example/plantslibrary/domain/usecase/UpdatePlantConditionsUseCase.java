package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;

public class UpdatePlantConditionsUseCase implements UpdatePlantConditionsUsePort {

    private final PlantRepositoryPort plantRepositoryPort;

    public UpdatePlantConditionsUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }

    @Override
    public Plant updateConditions(UpdateConditionsCommand command) {
        Plant plant = plantRepositoryPort.findById(command.plantId())
                .orElseThrow(() -> new PlantNotFoundException(command.plantId()));

        if (command.hydrationLevel() != null) {
            plant.setHydrationLevel(command.hydrationLevel());
        }
        if (command.humidityLevel() != null) {
            plant.setHumidityLevel(command.humidityLevel());
        }
        if (command.currentTemperature() != null) {
            plant.setCurrentTemperature(command.currentTemperature());
        }
        if (command.fertilizerNeeded() != null) {
            plant.setFertilizerNeeded(command.fertilizerNeeded());
        }

        return plantRepositoryPort.save(plant);
    }
}
