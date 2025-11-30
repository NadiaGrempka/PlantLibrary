package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.CareEventDocument;
import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MongoDB adapter for {@link CareEventRepositoryPort}.
 */
@Repository
public class CareEventRepositoryAdapter implements CareEventRepositoryPort {

    private final SpringDataCareEventRepository repository;

    public CareEventRepositoryAdapter(SpringDataCareEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public CareEvent save(CareEvent event) {
        CareEventDocument doc = toDocument(event);
        CareEventDocument saved = repository.save(doc);
        return toDomain(saved);
    }

    @Override
    public List<CareEvent> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CareEvent> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<CareEvent> findByPlantId(String plantId) {
        return repository.findByPlantId(plantId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CareEvent> findByStatus(CareStatus status) {
        return repository.findByStatus(status.name()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CareEvent> findScheduledBetween(java.time.Instant from, java.time.Instant to) {
        return repository.findByScheduledAtBetween(from, to).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private CareEventDocument toDocument(CareEvent event) {
        CareEventDocument doc = new CareEventDocument();
        doc.setId(event.getId());
        doc.setPlantId(event.getPlantId());
        doc.setUserId(event.getUserId());
        doc.setScheduledAt(event.getScheduledAt());
        doc.setCompletedAt(event.getCompletedAt());
        doc.setStatus(event.getStatus().name());
        doc.setNote(event.getNote());
        return doc;
    }

    private CareEvent toDomain(CareEventDocument doc) {
        CareEvent event = new CareEvent();
        event.setId(doc.getId());
        event.setPlantId(doc.getPlantId());
        event.setUserId(doc.getUserId());
        event.setScheduledAt(doc.getScheduledAt());
        event.setCompletedAt(doc.getCompletedAt());
        event.setStatus(CareStatus.valueOf(doc.getStatus()));
        event.setNote(doc.getNote());
        return event;
    }
}
