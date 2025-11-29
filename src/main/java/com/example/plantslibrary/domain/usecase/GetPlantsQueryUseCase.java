package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.GetPlantsQueryUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;

import java.util.List;

/**
 * Read-only use case for querying {@link Plant} aggregates.
 * <p>
 * Provides methods for fetching all plants, plants from a single room
 * and single plants by identifier.
 */
public class GetPlantsQueryUseCase implements GetPlantsQueryUsePort {

    private final PlantRepositoryPort plantRepositoryPort;

    /**
     * @param plantRepositoryPort port used to read plants from persistence layer
     */
    public GetPlantsQueryUseCase(PlantRepositoryPort plantRepositoryPort) {
        this.plantRepositoryPort = plantRepositoryPort;
    }


    /**
     * Returns all plants stored in the system.
     *
     * @return list of all {@link Plant} entities
     */
    @Override
    public List<Plant> getAll() {
        return plantRepositoryPort.findAll();
    }


    /**
     * Returns plants that belong to a specific room.
     *
     * @param roomId identifier of the room
     * @return list of plants assigned to the given room
     */
    @Override
    public List<Plant> getByRoom(String roomId) {
        return plantRepositoryPort.findByRoomId(roomId);
    }


    /**
     * Finds a plant by its identifier.
     *
     * @param id technical identifier of the plant
     * @return found {@link Plant}
     * @throws PlantNotFoundException when plant does not exist
     */
    @Override
    public Plant getById(String id) {
        return plantRepositoryPort.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
    }

    /**
     * Deletes a plant by identifier.
     *
     * @param id technical identifier of the plant to delete
     */
    @Override
    public void deleteById(String id) {
        plantRepositoryPort.deleteById(id);
    }
}
