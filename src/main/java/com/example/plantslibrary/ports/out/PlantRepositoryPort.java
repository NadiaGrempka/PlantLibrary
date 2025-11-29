package com.example.plantslibrary.ports.out;

import com.example.plantslibrary.domain.model.Plant;

import java.util.List;
import java.util.Optional;


/**
 * Outgoing port defining operations required from the persistence layer
 * for managing {@link Plant} aggregates.
 */
public interface PlantRepositoryPort {

    /**
     * Persists the given plant entity.
     *
     * @param plant plant to be saved
     * @return persisted plant, usually with generated identifier
     */
    Plant save(Plant plant);

    /**
     * Finds plant by its identifier.
     *
     * @param id technical identifier of the plant
     * @return {@link Optional} with plant or empty if not found
     */
    Optional<Plant> findById(String id);

    /**
     * Returns all plants.
     *
     * @return list of all plants
     */
    List<Plant> findAll();

    /**
     * Returns plants assigned to the given room.
     *
     * @param roomId identifier of the room
     * @return list of plants from the given room
     */
    List<Plant> findByRoomId(String roomId);

    /**
     * Deletes plant with the given identifier.
     *
     * @param id identifier of the plant to delete
     */
    void deleteById(String id);

    List<PlantNameView> findNamesByRoomId(String roomId);
    record PlantNameView(String id, String name) {}

}