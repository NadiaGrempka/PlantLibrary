package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;


/**
 * Use case responsible for updating environmental conditions of existing plants.
 * <p>
 * Supports partial updates – only non-null fields from the command are applied.
 */
public class UpdatePlantConditionsUseCase implements UpdatePlantConditionsUsePort {

    private final PlantRepositoryPort plantRepositoryPort;

    /**
     * @param plantRepositoryPort port used to load and persist plants
     */
    public UpdatePlantConditionsUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }

    /**
     * Updates conditions of an existing {@link Plant}.
     *
     * @param command immutable value object with optional fields to update
     * @return updated {@link Plant}
     * @throws PlantNotFoundException if plant with the given id does not exist
     */
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
