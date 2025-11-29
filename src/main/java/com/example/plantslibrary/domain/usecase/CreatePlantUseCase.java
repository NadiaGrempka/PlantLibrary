package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;


/**
 * Application service responsible for creating new {@link Plant} aggregates.
 * <p>
 * It maps {@link CreatePlantUsePort.CreatePlantCommand} into a domain model,
 * derives additional attributes (e.g. {@link TemperatureComfort}) and
 * delegates persistence to {@link PlantRepositoryPort}.
 */
public class CreatePlantUseCase implements CreatePlantUsePort {

    private final PlantRepositoryPort plantRepositoryPort;


    /**
     * Creates a new instance of {@link CreatePlantUseCase}.
     *
     * @param plantRepositoryPort port used to persist plants
     */
    public CreatePlantUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }


    /**
     * Creates a new {@link Plant} based on the provided command.
     *
     * @param command immutable value object with all data required to create a plant
     * @return persisted {@link Plant} with generated identifier
     */
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


    /**
     * Maps raw temperature value to a high-level {@link TemperatureComfort} category.
     *
     * @param temperature current temperature in degrees Celsius
     * @return comfort classification used by the UI and business logic
     */
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
