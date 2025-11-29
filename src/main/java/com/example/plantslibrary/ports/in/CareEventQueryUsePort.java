package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;

import java.util.List;

/**
 * Read-side port for querying care events.
 */
public interface CareEventQueryUsePort {
    List<CareEvent> getAll();
    List<CareEvent> getByPlant(String plantId);
    List<CareEvent> getByStatus(CareStatus status);
}
