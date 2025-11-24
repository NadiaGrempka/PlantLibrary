package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPlantsQueryUseCaseTest {

    @Mock
    PlantRepositoryPort plantRepositoryPort;

    @InjectMocks
    GetPlantsQueryUseCase useCase;

    @Test
    void getById_existingPlant_shouldReturnPlant() {
        Plant plant = new Plant();
        plant.setId("id-1");
        plant.setName("Monstera");

        when(plantRepositoryPort.findById("id-1"))
                .thenReturn(Optional.of(plant));

        Plant result = useCase.getById("id-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id-1");
        assertThat(result.getName()).isEqualTo("Monstera");
    }

    @Test
    void getById_notExisting_shouldThrowPlantNotFoundException() {
        when(plantRepositoryPort.findById("missing"))
                .thenReturn(Optional.empty());

        assertThrows(PlantNotFoundException.class,
                () -> useCase.getById("missing"));
    }
}
