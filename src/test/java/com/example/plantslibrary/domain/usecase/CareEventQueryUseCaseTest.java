package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareEventQueryUseCaseTest {

    @Mock
    private CareEventRepositoryPort repository;

    private CareEventQueryUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CareEventQueryUseCase(repository);
    }

    private CareEvent sampleEvent() {
        CareEvent e = new CareEvent();
        e.setId("ev-1");
        e.setPlantId("plant-1");
        e.setUserId("user-1");
        e.setStatus(CareStatus.WATERING);
        e.setScheduledAt(Instant.parse("2025-11-30T10:15:30Z"));
        e.setNote("Sample");
        return e;
    }

    @Test
    void getAll_shouldDelegateToRepository() {
        List<CareEvent> events = List.of(sampleEvent());
        when(repository.findAll()).thenReturn(events);

        List<CareEvent> result = useCase.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo("ev-1");
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getByPlant_shouldDelegateToRepositoryWithCorrectPlantId() {
        List<CareEvent> events = List.of(sampleEvent());
        when(repository.findByPlantId("plant-42")).thenReturn(events);

        List<CareEvent> result = useCase.getByPlant("plant-42");

        assertThat(result).hasSize(1);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(repository).findByPlantId(captor.capture());
        assertThat(captor.getValue()).isEqualTo("plant-42");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getByStatus_shouldDelegateToRepositoryWithCorrectStatus() {
        List<CareEvent> events = List.of(sampleEvent());
        when(repository.findByStatus(CareStatus.FERTILIZING)).thenReturn(events);

        List<CareEvent> result = useCase.getByStatus(CareStatus.FERTILIZING);

        assertThat(result).hasSize(1);
        verify(repository).findByStatus(CareStatus.FERTILIZING);
        verifyNoMoreInteractions(repository);
    }
}
