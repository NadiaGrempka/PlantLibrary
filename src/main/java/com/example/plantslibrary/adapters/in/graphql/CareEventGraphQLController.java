package com.example.plantslibrary.adapters.in.graphql;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.CareEventQueryUsePort;
import com.example.plantslibrary.ports.in.CareEventCommandUsePort;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * GraphQL entry point for querying and mutating care events and plants.
 */
@Controller
public class CareEventGraphQLController {

    private final CareEventQueryUsePort careQuery;
    private final CareEventCommandUsePort careCommand;

    public CareEventGraphQLController(CareEventQueryUsePort careQuery,
                                      CareEventCommandUsePort careCommand) {
        this.careQuery = careQuery;
        this.careCommand = careCommand;
    }

    // ---- QUERIES ----

    @QueryMapping
    public List<CareEvent> careEvents(@Argument CareStatus status,
                                      @Argument String plantId) {
        if (plantId != null) {
            return careQuery.getByPlant(plantId);
        }
        if (status != null) {
            return careQuery.getByStatus(status);
        }
        return careQuery.getAll();
    }

    // ---- MUTATIONS ----

    @MutationMapping
    public CareEvent createCareEvent(@Argument CreateCareEventInput input) {
        return careCommand.create(
                input.plantId(),
                input.userId(),
                input.scheduledAt(),
                input.note()
        );
    }

    @MutationMapping
    public CareEvent updateCareEvent(@Argument UpdateCareEventInput input) {
        return careCommand.update(
                input.id(),
                input.scheduledAt(),
                input.completedAt(),
                input.status(),
                input.note()
        );
    }

    @MutationMapping
    public CareEvent cancelCareEvent(@Argument String id) {
        return careCommand.cancel(id);
    }

    // ---- N+1 optimization: CareEvent.plant ----

    @SchemaMapping(typeName = "CareEvent", field = "plant")
    public CompletableFuture<Plant> plant(CareEvent careEvent, DataLoader<String, Plant> dataLoader) {
        return dataLoader.load(careEvent.getPlantId());
    }



    // --- local records for GraphQL inputs ---

    public record CreateCareEventInput(String plantId, String userId, Instant scheduledAt, String note) {}
    public record UpdateCareEventInput(String id, Instant scheduledAt, Instant completedAt, CareStatus status, String note) {}
}
