package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.GetPlantsQueryUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;

import java.util.List;
import java.util.Optional;

public class GetPlantsQueryUseCase implements GetPlantsQueryUsePort {

    private final PlantRepositoryPort plantRepositoryPort;

    public GetPlantsQueryUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }

    @Override
    public List<Plant> getAll() {
        return plantRepositoryPort.findAll();
    }

    @Override
    public List<Plant> getByRoom(String roomId) {
        return plantRepositoryPort.findByRoomId(roomId);
    }

    @Override
    public Plant getById(String id) {
        return plantRepositoryPort.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
    }

    @Override
    public void deleteById(String id) {
        plantRepositoryPort.deleteById(id);
    }
}
