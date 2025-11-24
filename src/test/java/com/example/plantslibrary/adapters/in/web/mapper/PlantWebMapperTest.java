package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.in.web.dto.PlantResponse;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlantWebMapperTest {

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        // given
        Plant plant = new Plant();
        plant.setId("plant-123");
        plant.setName("Monstera");
        plant.setHydrationLevel(60);
        plant.setHumidityLevel(50);
        plant.setSunlightLevel(SunlightLevel.MEDIUM);
        plant.setFertilizerNeeded(true);
        plant.setCurrentTemperature(23.5);
        plant.setTemperatureComfort(TemperatureComfort.OK);
        plant.setRoomId("room-xyz");

        // when
        PlantResponse resp = PlantWebMapper.toResponse(plant);

        // then
        assertThat(resp.getId()).isEqualTo("plant-123");
        assertThat(resp.getName()).isEqualTo("Monstera");
        assertThat(resp.getHydrationLevel()).isEqualTo(60);
        assertThat(resp.getHumidityLevel()).isEqualTo(50);
        assertThat(resp.getSunlightLevel()).isEqualTo("MEDIUM");
        assertThat(resp.isFertilizerNeeded()).isTrue();
        assertThat(resp.getCurrentTemperature()).isEqualTo(23.5);
        assertThat(resp.getTemperatureComfort()).isEqualTo("OK");
        assertThat(resp.getRoomId()).isEqualTo("room-xyz");
    }
}
