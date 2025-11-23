package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Plant;

public interface UpdatePlantConditionsUsePort {

    Plant updateConditions(UpdateConditionsCommand command);

    record UpdateConditionsCommand(
            String plantId,
            Integer hydrationLevel,
            Integer humidityLevel,
            Double currentTemperature,
            Boolean fertilizerNeeded
    ) {}
}
