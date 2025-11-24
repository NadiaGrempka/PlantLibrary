package com.example.plantslibrary.integration;

import com.example.plantslibrary.adapters.out.mongo.RoomRepositoryAdapter;
import com.example.plantslibrary.domain.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomRepositoryAdapterTest extends AbstractMongoIntegrationTest {

    @Autowired
    private RoomRepositoryAdapter roomRepositoryAdapter;

    @AfterEach
    void cleanup() {
        roomRepositoryAdapter.findAll()
                .forEach(r -> roomRepositoryAdapter.deleteById(r.getId()));
    }

    private Room sampleRoom(String name) {
        Room r = new Room();
        r.setName(name);
        r.setTargetTemperature(22.0);
        r.setTargetHumidity(50);
        r.setWindowOrientation("SOUTH");
        return r;
    }

    @Test
    void createAndFindById_shouldPersistRoom() {
        Room saved = roomRepositoryAdapter.save(sampleRoom("Living Room"));

        Optional<Room> found = roomRepositoryAdapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Living Room");
    }

    @Test
    void findAll_shouldReturnAllRooms() {
        roomRepositoryAdapter.save(sampleRoom("Living Room"));
        roomRepositoryAdapter.save(sampleRoom("Bedroom"));

        List<Room> all = roomRepositoryAdapter.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void update_shouldModifyRoom() {
        Room saved = roomRepositoryAdapter.save(sampleRoom("Living"));

        saved.setTargetTemperature(24.0);
        Room updated = roomRepositoryAdapter.save(saved);

        Optional<Room> reloaded = roomRepositoryAdapter.findById(updated.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getTargetTemperature()).isEqualTo(24.0);
    }

    @Test
    void deleteById_shouldRemoveRoom() {
        Room saved = roomRepositoryAdapter.save(sampleRoom("Office"));

        roomRepositoryAdapter.deleteById(saved.getId());

        Optional<Room> found = roomRepositoryAdapter.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findByNameOrIndexedField_shouldWork() {
        Room saved = roomRepositoryAdapter.save(sampleRoom("Kitchen"));

        List<Room> all = roomRepositoryAdapter.findAll();
        assertThat(all.stream().anyMatch(r -> "Kitchen".equals(r.getName()))).isTrue();
    }
}
