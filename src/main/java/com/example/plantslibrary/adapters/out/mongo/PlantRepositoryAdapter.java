package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MongoDB adapter implementing {@link PlantRepositoryPort} using
 * Spring Data {@link SpringDataPlantRepository}.
 */
@Repository
public class PlantRepositoryAdapter implements PlantRepositoryPort {

    private final SpringDataPlantRepository springDataPlantRepository;

    /**
     * @param springDataPlantRepository Spring Data repository for {@link PlantDocument}
     */
    public PlantRepositoryAdapter(SpringDataPlantRepository springDataPlantRepository) {
        this.springDataPlantRepository = springDataPlantRepository;
    }

    @Override
    public Plant save(Plant plant) {
        PlantDocument doc = toDocument(plant);
        PlantDocument saved = springDataPlantRepository.save(doc);
        return toDomain(saved);
    }

    @Override
    public Optional<Plant> findById(String id) {
        return springDataPlantRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Plant> findAll() {
        return springDataPlantRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Plant> findByRoomId(String roomId) {
        return springDataPlantRepository.findByRoomId(roomId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        springDataPlantRepository.deleteById(id);
    }

    @Override
    public List<PlantNameView> findNamesByRoomId(String roomId) {
        return List.of();
    }

    // mapowanie

    /**
     * Maps domain {@link Plant} to Mongo {@link PlantDocument}.
     */
    private PlantDocument toDocument(Plant plant) {
        PlantDocument doc = new PlantDocument();
        doc.setId(plant.getId());
        doc.setName(plant.getName());
        doc.setHydrationLevel(plant.getHydrationLevel());
        doc.setHumidityLevel(plant.getHumidityLevel());
        doc.setSunlightLevel(plant.getSunlightLevel().name());
        doc.setFertilizerNeeded(plant.isFertilizerNeeded());
        doc.setCurrentTemperature(plant.getCurrentTemperature());
        doc.setTemperatureComfort(
                plant.getTemperatureComfort() != null
                        ? plant.getTemperatureComfort().name()
                        : null
        );
        doc.setRoomId(plant.getRoomId());
        return doc;
    }

    /**
     * Maps Mongo {@link PlantDocument} to domain {@link Plant}.
     */
    private Plant toDomain(PlantDocument doc) {
        Plant plant = new Plant();
        plant.setId(doc.getId());
        plant.setName(doc.getName());
        plant.setHydrationLevel(doc.getHydrationLevel());
        plant.setHumidityLevel(doc.getHumidityLevel());
        plant.setSunlightLevel(SunlightLevel.valueOf(doc.getSunlightLevel()));
        plant.setFertilizerNeeded(doc.getFertilizerNeeded());
        plant.setCurrentTemperature(doc.getCurrentTemperature());

        if (doc.getTemperatureComfort() != null) {
            plant.setTemperatureComfort(
                    TemperatureComfort.valueOf(doc.getTemperatureComfort())
            );
        }

        plant.setRoomId(doc.getRoomId());
        return plant;
    }
}
