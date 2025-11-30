package com.example.plantslibrary.adapters.out.mongo.document;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class MongoDocumentsTest {

    @Test
    void roomDocument_gettersAndSetters_shouldWork() {
        RoomDocument room = new RoomDocument();
        room.setId("room-1");
        room.setName("Salon");
        room.setTargetTemperature(22.0);
        room.setTargetHumidity(45);
        room.setWindowOrientation("SOUTH");

        assertThat(room.getId()).isEqualTo("room-1");
        assertThat(room.getName()).isEqualTo("Salon");
        assertThat(room.getTargetTemperature()).isEqualTo(22.0);
        assertThat(room.getTargetHumidity()).isEqualTo(45);
        assertThat(room.getWindowOrientation()).isEqualTo("SOUTH");
    }

    @Test
    void plantDocument_gettersAndSetters_shouldWork() {
        PlantDocument plant = new PlantDocument();
        plant.setId("plant-1");
        plant.setName("Monstera");
        plant.setHydrationLevel(70);
        plant.setHumidityLevel(60);
        plant.setSunlightLevel("MEDIUM");
        plant.setFertilizerNeeded(false);
        plant.setCurrentTemperature(22.5);
        plant.setTemperatureComfort("OK");
        plant.setRoomId("room-1");

        assertThat(plant.getId()).isEqualTo("plant-1");
        assertThat(plant.getName()).isEqualTo("Monstera");
        assertThat(plant.getHydrationLevel()).isEqualTo(70);
        assertThat(plant.getHumidityLevel()).isEqualTo(60);
        assertThat(plant.getSunlightLevel()).isEqualTo("MEDIUM");
        assertThat(plant.getFertilizerNeeded()).isFalse();
        assertThat(plant.getCurrentTemperature()).isEqualTo(22.5);
        assertThat(plant.getTemperatureComfort()).isEqualTo("OK");
        assertThat(plant.getRoomId()).isEqualTo("room-1");
    }

    @Test
    void careEventDocument_gettersAndSetters_shouldWork() {
        CareEventDocument event = new CareEventDocument();
        event.setId("event-1");
        event.setPlantId("plant-1");
        event.setUserId("user-1");
        Instant scheduled = Instant.parse("2025-11-30T10:00:00Z");
        Instant completed = Instant.parse("2025-11-30T12:00:00Z");
        event.setScheduledAt(scheduled);
        event.setCompletedAt(completed);
        event.setStatus("WATERING");
        event.setNote("Test note");

        assertThat(event.getId()).isEqualTo("event-1");
        assertThat(event.getPlantId()).isEqualTo("plant-1");
        assertThat(event.getUserId()).isEqualTo("user-1");
        assertThat(event.getScheduledAt()).isEqualTo(scheduled);
        assertThat(event.getCompletedAt()).isEqualTo(completed);
        assertThat(event.getStatus()).isEqualTo("WATERING");
        assertThat(event.getNote()).isEqualTo("Test note");
    }
}
