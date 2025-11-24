package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePlantUseCaseTest {

    @Mock
    PlantRepositoryPort plantRepositoryPort;

    @InjectMocks
    CreatePlantUseCase createPlantUseCase;

    @Test
    void createPlant_withColdTemperature_setsTooCold() {
        // given
        CreatePlantUsePort.CreatePlantCommand cmd =
                new CreatePlantUsePort.CreatePlantCommand(
                        "ColdPlant",
                        60,
                        50,
                        "LOW",
                        false,
                        10.0,          // < 15 -> TOO_COLD
                        "room-1"
                );

        when(plantRepositoryPort.save(any(Plant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Plant result = createPlantUseCase.createPlant(cmd);

        // then
        assertThat(result.getTemperatureComfort())
                .isEqualTo(TemperatureComfort.TOO_COLD);
    }

    @Test
    void createPlant_withHotTemperature_setsTooHot() {
        // given
        CreatePlantUsePort.CreatePlantCommand cmd =
                new CreatePlantUsePort.CreatePlantCommand(
                        "HotPlant",
                        60,
                        50,
                        "HIGH",
                        false,
                        30.0,
                        "room-1"
                );

        when(plantRepositoryPort.save(any(Plant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Plant result = createPlantUseCase.createPlant(cmd);

        // then
        assertThat(result.getTemperatureComfort())
                .isEqualTo(TemperatureComfort.TOO_HOT);
    }
}
