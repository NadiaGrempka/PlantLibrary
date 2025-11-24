package com.example.plantslibrary.integration;

import com.example.plantslibrary.adapters.out.mongo.PlantRepositoryAdapter;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantRepositoryAdapterTest extends AbstractMongoIntegrationTest {

    @Autowired
    private PlantRepositoryAdapter plantRepositoryAdapter;

    @AfterEach
    void cleanup() {
        plantRepositoryAdapter.findAll()
                .forEach(p -> plantRepositoryAdapter.deleteById(p.getId()));
    }

    private Plant samplePlant(String name, String roomId) {
        Plant p = new Plant();
        p.setName(name);
        p.setHydrationLevel(60);
        p.setHumidityLevel(50);
        p.setSunlightLevel(SunlightLevel.MEDIUM);
        p.setFertilizerNeeded(false);
        p.setCurrentTemperature(22.0);
        p.setTemperatureComfort(TemperatureComfort.OK);
        p.setRoomId(roomId);
        return p;
    }

    @Test
    void createAndFindById_shouldPersistPlant() {
        Plant toSave = samplePlant("Monstera-IT", "room-1");

        Plant saved = plantRepositoryAdapter.save(toSave);
        Optional<Plant> found = plantRepositoryAdapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Monstera-IT");
        assertThat(found.get().getId()).isNotNull();
    }

    @Test
    void findAll_shouldReturnAllPlants() {
        plantRepositoryAdapter.save(samplePlant("P1", "room-1"));
        plantRepositoryAdapter.save(samplePlant("P2", "room-2"));

        List<Plant> all = plantRepositoryAdapter.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void findByRoomId_shouldFilterByRoom() {
        plantRepositoryAdapter.save(samplePlant("P1", "room-1"));
        plantRepositoryAdapter.save(samplePlant("P2", "room-2"));

        List<Plant> room1 = plantRepositoryAdapter.findByRoomId("room-1");

        assertThat(room1).hasSize(1);
        assertThat(room1.get(0).getName()).isEqualTo("P1");
    }

    @Test
    void update_shouldModifyExistingPlant() {
        Plant saved = plantRepositoryAdapter.save(samplePlant("P1", "room-1"));

        saved.setHydrationLevel(80);
        Plant updated = plantRepositoryAdapter.save(saved);

        Optional<Plant> reloaded = plantRepositoryAdapter.findById(updated.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getHydrationLevel()).isEqualTo(80);
    }

    @Test
    void deleteById_shouldRemovePlant() {
        Plant saved = plantRepositoryAdapter.save(samplePlant("P1", "room-1"));

        plantRepositoryAdapter.deleteById(saved.getId());

        Optional<Plant> found = plantRepositoryAdapter.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
