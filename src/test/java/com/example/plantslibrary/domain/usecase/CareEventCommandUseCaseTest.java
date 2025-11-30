package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareEventCommandUseCaseTest {

    @Mock
    CareEventRepositoryPort repository;

    @InjectMocks
    CareEventCommandUseCase useCase;

    @Test
    void create_shouldBuildEventAndSave() {
        // given
        Instant scheduled = Instant.parse("2025-11-30T10:00:00Z");

        when(repository.save(any(CareEvent.class)))
                .thenAnswer(invocation -> {
                    CareEvent e = invocation.getArgument(0);
                    e.setId("event-1");
                    return e;
                });

        // when
        CareEvent result = useCase.create("plant-1", "user-1", scheduled, "note");

        // then
        assertEquals("event-1", result.getId());
        assertEquals("plant-1", result.getPlantId());
        assertEquals("user-1", result.getUserId());
        assertEquals(scheduled, result.getScheduledAt());
        assertEquals(CareStatus.SCHEDULED, result.getStatus());
        assertEquals("note", result.getNote());
    }

    @Test
    void update_shouldModifyNonNullFieldsAndSave() {
        // given
        CareEvent existing = new CareEvent();
        existing.setId("event-1");
        existing.setPlantId("plant-1");
        existing.setUserId("user-1");
        existing.setStatus(CareStatus.SCHEDULED);
        existing.setNote("old note");

        when(repository.findById("event-1")).thenReturn(Optional.of(existing));
        when(repository.save(any(CareEvent.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Instant newScheduled = Instant.parse("2025-12-01T09:00:00Z");
        Instant completed = Instant.parse("2025-12-01T10:00:00Z");

        // when
        CareEvent result = useCase.update(
                "event-1",
                newScheduled,
                completed,
                CareStatus.WATERING,
                "new note"
        );

        // then
        assertEquals("event-1", result.getId());
        assertEquals(newScheduled, result.getScheduledAt());
        assertEquals(completed, result.getCompletedAt());
        assertEquals(CareStatus.WATERING, result.getStatus());
        assertEquals("new note", result.getNote());
    }

    @Test
    void update_shouldLeaveFieldsUntouchedWhenNullArguments() {
        // given
        CareEvent existing = new CareEvent();
        existing.setId("event-1");
        existing.setPlantId("plant-1");
        existing.setUserId("user-1");
        existing.setStatus(CareStatus.SCHEDULED);
        existing.setNote("old note");
        Instant oldScheduled = Instant.parse("2025-11-30T10:00:00Z");
        existing.setScheduledAt(oldScheduled);

        when(repository.findById("event-1")).thenReturn(Optional.of(existing));
        when(repository.save(any(CareEvent.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        CareEvent result = useCase.update(
                "event-1",
                null,
                null,
                CareStatus.CANCELLED,
                null
        );

        // then
        assertEquals(oldScheduled, result.getScheduledAt());
        assertNull(result.getCompletedAt());
        assertEquals(CareStatus.CANCELLED, result.getStatus());
        assertEquals("old note", result.getNote());
    }

    @Test
    void update_shouldThrowWhenEventNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.update("missing", null, null, null, null));

        verify(repository, never()).save(any());
    }

    @Test
    void cancel_shouldSetStatusCancelledAndSave() {
        // given
        CareEvent existing = new CareEvent();
        existing.setId("event-1");
        existing.setStatus(CareStatus.SCHEDULED);

        when(repository.findById("event-1")).thenReturn(Optional.of(existing));
        when(repository.save(any(CareEvent.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        CareEvent result = useCase.cancel("event-1");

        // then
        assertEquals(CareStatus.CANCELLED, result.getStatus());
        verify(repository).save(existing);
    }

    @Test
    void cancel_shouldThrowWhenEventNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.cancel("missing"));
        verify(repository, never()).save(any());
    }
}
