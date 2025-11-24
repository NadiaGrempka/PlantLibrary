package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePlantConditionsUseCaseTest {

    @Mock
    PlantRepositoryPort plantRepositoryPort;

    @InjectMocks
    UpdatePlantConditionsUseCase updatePlantConditionsUseCase;

    private Plant samplePlant() {
        Plant p = new Plant();
        p.setId("plant-1");
        p.setName("Monstera");
        p.setHydrationLevel(50);
        p.setHumidityLevel(40);
        p.setSunlightLevel(SunlightLevel.MEDIUM);
        p.setFertilizerNeeded(false);
        p.setCurrentTemperature(22.0);
        p.setTemperatureComfort(TemperatureComfort.OK);
        p.setRoomId("room-1");
        return p;
    }

    @Test
    void updateConditions_onlyHumidity_shouldChangeHumidityOnly() {
        // given
        Plant existing = samplePlant();

        when(plantRepositoryPort.findById("plant-1"))
                .thenReturn(Optional.of(existing));
        when(plantRepositoryPort.save(any(Plant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePlantConditionsUsePort.UpdateConditionsCommand cmd =
                new UpdatePlantConditionsUsePort.UpdateConditionsCommand(
                        "plant-1",
                        null,   // hydrationLevel
                        80,     // humidityLevel
                        null,
                        null
                );

        // when
        Plant updated = updatePlantConditionsUseCase.updateConditions(cmd);

        // then
        assertThat(updated.getHydrationLevel()).isEqualTo(50);
        assertThat(updated.getHumidityLevel()).isEqualTo(80);
        assertThat(updated.isFertilizerNeeded()).isFalse();
    }

    @Test
    void updateConditions_onlyFertilizer_shouldChangeFertilizerOnly() {
        // given
        Plant existing = samplePlant();

        when(plantRepositoryPort.findById("plant-1"))
                .thenReturn(Optional.of(existing));
        when(plantRepositoryPort.save(any(Plant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePlantConditionsUsePort.UpdateConditionsCommand cmd =
                new UpdatePlantConditionsUsePort.UpdateConditionsCommand(
                        "plant-1",
                        null,
                        null,
                        null,
                        true
                );

        // when
        Plant updated = updatePlantConditionsUseCase.updateConditions(cmd);

        // then
        assertThat(updated.isFertilizerNeeded()).isTrue();
        assertThat(updated.getHumidityLevel()).isEqualTo(40);
        assertThat(updated.getHydrationLevel()).isEqualTo(50);
    }

    @Test
    void updateConditions_notExisting_shouldThrowPlantNotFound() {
        // given
        when(plantRepositoryPort.findById("missing"))
                .thenReturn(Optional.empty());

        UpdatePlantConditionsUsePort.UpdateConditionsCommand cmd =
                new UpdatePlantConditionsUsePort.UpdateConditionsCommand(
                        "missing",
                        70,
                        null,
                        null,
                        null
                );

        // when / then
        assertThatThrownBy(() -> updatePlantConditionsUseCase.updateConditions(cmd))
                .isInstanceOf(PlantNotFoundException.class);
    }
}
