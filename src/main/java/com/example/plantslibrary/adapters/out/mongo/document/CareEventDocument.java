package com.example.plantslibrary.adapters.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

/**
 * MongoDB document representing a single care event for a plant
 * (watering, fertilizing, inspection etc.).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "care_events")
public class CareEventDocument {

    @Id
    private String id;

    /** Id of the plant this event belongs to (for simple queries). */
    @Indexed
    private String plantId;

    /** Optional identifier of user / owner who created the event. */
    private String userId;

    /** Scheduled date/time of the care action. */
    @Indexed
    private Instant scheduledAt;

    /** When the action was actually completed (nullable). */
    private Instant completedAt;

    /** Status of the event: PLANNED, DONE, CANCELLED. */
    private String status;

    /** Optional human-readable note. */
    private String note;

    /**
     * Reference to the plant document – one plant can have many care events.
     * This demonstrates @DocumentReference relation in MongoDB.
     */
    @DocumentReference(lazy = true)
    private PlantDocument plant;
}
