package com.example.plantslibrary.ports.out;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Port for persisting and querying plant care events.
 */
public interface CareEventRepositoryPort {

    CareEvent save(CareEvent event);

    List<CareEvent> findAll();

    Optional<CareEvent> findById(String id);

    List<CareEvent> findByPlantId(String plantId);

    List<CareEvent> findByStatus(CareStatus status);

    List<CareEvent> findScheduledBetween(Instant from, Instant to);

    void deleteById(String id);
}
