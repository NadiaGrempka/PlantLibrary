package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.adapters.in.web.dto.PlantCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.PlantResponse;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.in.GetPlantsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PlantControllerTest {

    @Mock
    GetPlantsQueryUsePort getPlantsQueryUsePort;

    @Mock
    CreatePlantUsePort createPlantUsePort;

    @Mock
    UpdatePlantConditionsUsePort updatePlantConditionsUsePort;

    @InjectMocks
    PlantController controller;

    @Test
    void getById_shouldReturnPlant() {
        Plant plant = new Plant();
        plant.setId("plant-1");
        plant.setName("Monstera");
        plant.setHydrationLevel(70);
        plant.setHumidityLevel(60);
        plant.setSunlightLevel(SunlightLevel.MEDIUM);
        plant.setFertilizerNeeded(false);
        plant.setCurrentTemperature(22.5);
        plant.setTemperatureComfort(TemperatureComfort.OK);
        plant.setRoomId("room-1");

        // UŻYWAMY doReturn(...).when(...)
        doReturn(plant)
                .when(getPlantsQueryUsePort)
                .getById("plant-1");

        ResponseEntity<PlantResponse> response = controller.getById("plant-1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PlantResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo("plant-1");
        assertThat(body.getName()).isEqualTo("Monstera");
        assertThat(body.getSunlightLevel()).isEqualTo("MEDIUM");
        assertThat(body.getTemperatureComfort()).isEqualTo("OK");
    }

    @Test
    void create_shouldReturnCreated() {
        PlantCreateRequest req = new PlantCreateRequest();
        req.setName("Monstera");
        req.setRoomId("room-1");
        req.setHydrationLevel(70);
        req.setHumidityLevel(60);
        req.setSunlightLevel("MEDIUM");
        req.setFertilizerNeeded(false);
        req.setCurrentTemperature(22.5);

        Plant plant = new Plant();
        plant.setId("plant-1");
        plant.setName("Monstera");
        plant.setHydrationLevel(70);
        plant.setHumidityLevel(60);
        plant.setSunlightLevel(SunlightLevel.MEDIUM);
        plant.setFertilizerNeeded(false);
        plant.setCurrentTemperature(22.5);
        plant.setTemperatureComfort(TemperatureComfort.OK);
        plant.setRoomId("room-1");

        // Tu też użyjmy do Return dla spójności
        doReturn(plant)
                .when(createPlantUsePort)
                .createPlant(any());

        ResponseEntity<PlantResponse> response = controller.create(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        PlantResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo("plant-1");
        assertThat(body.getName()).isEqualTo("Monstera");
    }
}
