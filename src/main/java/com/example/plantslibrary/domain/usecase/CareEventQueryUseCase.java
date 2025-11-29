package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.ports.in.CareEventQueryUsePort;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service for reading care events.
 */
@Service
public class CareEventQueryUseCase implements CareEventQueryUsePort {

    private final CareEventRepositoryPort repository;

    public CareEventQueryUseCase(CareEventRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<CareEvent> getAll() {
        return repository.findAll();
    }

    @Override
    public List<CareEvent> getByPlant(String plantId) {
        return repository.findByPlantId(plantId);
    }

    @Override
    public List<CareEvent> getByStatus(CareStatus status) {
        return repository.findByStatus(status);
    }
}
