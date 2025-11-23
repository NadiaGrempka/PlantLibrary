package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Plant;


public interface CreatePlantUsePort {

    Plant createPlant(CreatePlantCommand command);

    record CreatePlantCommand(
            String name,
            int hydrationLevel,
            int humidityLevel,
            String sunlightLevel,
            boolean fertilizerNeeded,
            double currentTemperature,
            String roomId
    ) {}
}
