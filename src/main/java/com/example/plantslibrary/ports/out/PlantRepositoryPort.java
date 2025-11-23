package com.example.plantslibrary.ports.out;

import com.example.plantslibrary.domain.model.Plant;

import java.util.List;
import java.util.Optional;

public interface PlantRepositoryPort {

    Plant save(Plant plant);
    Optional<Plant> findById(String id);
    List<Plant> findAll();
    List<Plant> findByRoomId(String roomId);
    void deleteById(String id);

    List<PlantNameView> findNamesByRoomId(String roomId);
    record PlantNameView(String id, String name) {}

}