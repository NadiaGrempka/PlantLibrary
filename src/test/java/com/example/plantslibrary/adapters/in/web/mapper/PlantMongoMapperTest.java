package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantMongoMapperTest {

    @Test
    void toDomain_shouldMapAllFieldsCorrectly() {
        // given
        PlantDocument doc = new PlantDocument();
        doc.setId("plant-1");
        doc.setName("Monstera");
        doc.setHydrationLevel(70);
        doc.setHumidityLevel(60);
        doc.setSunlightLevel("MEDIUM");
        doc.setFertilizerNeeded(true);
        doc.setCurrentTemperature(22.5);
        doc.setTemperatureComfort("OK");
        doc.setRoomId("room-1");

        // when
        Plant plant = PlantMongoMapper.toDomain(doc);

        // then
        assertNotNull(plant);
        assertEquals("plant-1", plant.getId());
        assertEquals("Monstera", plant.getName());
        assertEquals(70, plant.getHydrationLevel());
        assertEquals(60, plant.getHumidityLevel());
        assertEquals(SunlightLevel.MEDIUM, plant.getSunlightLevel());
        assertTrue(plant.isFertilizerNeeded());
        assertEquals(22.5, plant.getCurrentTemperature());
        assertEquals(TemperatureComfort.OK, plant.getTemperatureComfort());
        assertEquals("room-1", plant.getRoomId());
    }
}
