package com.example.plantslibrary.adapters.in.graphql;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.CareEventCommandUsePort;
import com.example.plantslibrary.ports.in.CareEventQueryUsePort;
import org.dataloader.DataLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareEventGraphQLControllerTest {

    @Mock
    CareEventQueryUsePort careQuery;

    @Mock
    CareEventCommandUsePort careCommand;

    @InjectMocks
    CareEventGraphQLController controller;

    private CareEvent sampleEvent() {
        CareEvent e = new CareEvent();
        e.setId("event-1");
        e.setPlantId("plant-1");
        e.setUserId("user-1");
        e.setStatus(CareStatus.WATERING);
        e.setScheduledAt(Instant.parse("2025-11-30T10:00:00Z"));
        e.setNote("GraphQL test");
        return e;
    }

    @Test
    void careEvents_withoutArguments_shouldCallGetAll() {
        CareEvent event = sampleEvent();
        when(careQuery.getAll()).thenReturn(List.of(event));

        List<CareEvent> result = controller.careEvents(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("event-1");
        verify(careQuery).getAll();
        verifyNoMoreInteractions(careQuery);
    }

    @Test
    void careEvents_withPlantId_shouldCallGetByPlant() {
        CareEvent event = sampleEvent();
        when(careQuery.getByPlant("plant-xyz")).thenReturn(List.of(event));

        List<CareEvent> result = controller.careEvents(null, "plant-xyz");

        assertThat(result).hasSize(1);
        verify(careQuery).getByPlant("plant-xyz");
        verifyNoMoreInteractions(careQuery);
    }

    @Test
    void careEvents_withStatus_shouldCallGetByStatus() {
        CareEvent event = sampleEvent();
        when(careQuery.getByStatus(CareStatus.WATERING)).thenReturn(List.of(event));

        List<CareEvent> result = controller.careEvents(CareStatus.WATERING, null);

        assertThat(result).hasSize(1);
        verify(careQuery).getByStatus(CareStatus.WATERING);
        verifyNoMoreInteractions(careQuery);
    }

    @Test
    void createCareEvent_shouldDelegateToUseCase() {
        CareEvent created = sampleEvent();
        created.setId("new-id");

        Instant scheduled = Instant.parse("2025-11-30T10:00:00Z");

        when(careCommand.create("plant-1", "user-1", scheduled, "note from mutation"))
                .thenReturn(created);

        CareEventGraphQLController.CreateCareEventInput input =
                new CareEventGraphQLController.CreateCareEventInput(
                        "plant-1",
                        "user-1",
                        scheduled,
                        "note from mutation"
                );

        CareEvent result = controller.createCareEvent(input);

        assertThat(result.getId()).isEqualTo("new-id");
        assertThat(result.getNote()).isEqualTo(created.getNote());

        verify(careCommand).create("plant-1", "user-1", scheduled, "note from mutation");
        verifyNoMoreInteractions(careCommand);
    }

    @Test
    void plantField_shouldReturnCompletableFuture() {
        // given
        CareEvent event = sampleEvent();

        Plant plant = new Plant();
        plant.setId("plant-1");
        plant.setName("Monstera");

        // mock Data Loader zamiast prawdziwego
        @SuppressWarnings("unchecked")
        DataLoader<String, Plant> loader = mock(DataLoader.class);

        when(loader.load("plant-1"))
                .thenReturn(CompletableFuture.completedFuture(plant));

        // when
        CompletableFuture<Plant> future = controller.plant(event, loader);
        Plant result = future.join();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("plant-1");
        assertThat(result.getName()).isEqualTo("Monstera");
        verify(loader).load("plant-1");
    }

    @Test
    void updateCareEvent_shouldDelegateToUseCase() {
        // given
        CareEvent updated = sampleEvent();
        Instant newScheduled = Instant.parse("2025-12-01T08:00:00Z");
        Instant completed = Instant.parse("2025-12-02T09:00:00Z");

        updated.setScheduledAt(newScheduled);
        updated.setCompletedAt(completed);
        updated.setStatus(CareStatus.FERTILIZING);
        updated.setNote("updated note");

        when(careCommand.update(
                "event-1",
                newScheduled,
                completed,
                CareStatus.FERTILIZING,
                "updated note"
        )).thenReturn(updated);

        CareEventGraphQLController.UpdateCareEventInput input =
                new CareEventGraphQLController.UpdateCareEventInput(
                        "event-1",
                        newScheduled,
                        completed,
                        CareStatus.FERTILIZING,
                        "updated note"
                );

        // when
        CareEvent result = controller.updateCareEvent(input);

        // then
        assertThat(result).isSameAs(updated);
        assertThat(result.getStatus()).isEqualTo(CareStatus.FERTILIZING);
        assertThat(result.getNote()).isEqualTo("updated note");

        verify(careCommand).update(
                "event-1",
                newScheduled,
                completed,
                CareStatus.FERTILIZING,
                "updated note"
        );
        verifyNoMoreInteractions(careCommand);
    }


    @Test
    void cancelCareEvent_shouldDelegateToUseCase() {
        // given
        CareEvent cancelled = sampleEvent();
        cancelled.setStatus(CareStatus.CANCELLED);

        when(careCommand.cancel("event-1")).thenReturn(cancelled);

        // when
        CareEvent result = controller.cancelCareEvent("event-1");

        // then
        assertThat(result.getStatus()).isEqualTo(CareStatus.CANCELLED);

        verify(careCommand).cancel("event-1");
        verifyNoMoreInteractions(careCommand);
    }

}
