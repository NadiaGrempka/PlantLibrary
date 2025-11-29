package com.example.plantslibrary.ports.in;


import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;

import java.time.Instant;

/**
 * Write-side port for creating and modifying care events.
 */
public interface CareEventCommandUsePort {

    CareEvent create(String plantId,
                     String userId,
                     Instant scheduledAt,
                     String note);

    CareEvent update(String id,
                     Instant scheduledAt,
                     Instant completedAt,
                     CareStatus status,
                     String note);

    CareEvent cancel(String id);
}
