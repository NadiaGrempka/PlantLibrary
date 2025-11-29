package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.ports.in.CareEventCommandUsePort;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Application service for creating and modifying care events.
 */
@Service
public class CareEventCommandUseCase implements CareEventCommandUsePort {

    private final CareEventRepositoryPort repository;

    public CareEventCommandUseCase(CareEventRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public CareEvent create(String plantId, String userId, Instant scheduledAt, String note) {
        CareEvent event = new CareEvent();
        event.setPlantId(plantId);
        event.setUserId(userId);
        event.setScheduledAt(scheduledAt);
        event.setStatus(CareStatus.SCHEDULED);
        event.setNote(note);
        return repository.save(event);
    }

    @Override
    public CareEvent update(String id,
                            Instant scheduledAt,
                            Instant completedAt,
                            CareStatus status,
                            String note) {

        CareEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CareEvent with id " + id + " not found")); // możesz potem zrobić własny wyjątek

        if (scheduledAt != null) {
            event.setScheduledAt(scheduledAt);
        }
        if (completedAt != null) {
            event.setCompletedAt(completedAt);
        }
        if (status != null) {
            event.setStatus(status);
        }
        if (note != null) {
            event.setNote(note);
        }
        return repository.save(event);
    }

    @Override
    public CareEvent cancel(String id) {
        CareEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CareEvent with id " + id + " not found"));
        event.setStatus(CareStatus.CANCELLED);
        return repository.save(event);
    }
}
