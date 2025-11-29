package com.example.plantslibrary.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Domain model of a single plant care event.
 */
@Getter
@Setter
@NoArgsConstructor
public class CareEvent {

    private String id;
    private String plantId;
    private String userId;
    private Instant scheduledAt;
    private Instant completedAt;
    private CareStatus status;
    private String note;

}
