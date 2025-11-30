package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.CareEventDocument;
import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareEventRepositoryAdapterTest {

    @Mock
    private SpringDataCareEventRepository springRepo;

    private CareEventRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CareEventRepositoryAdapter(springRepo);
    }

    private CareEventDocument sampleDoc() {
        CareEventDocument doc = new CareEventDocument();
        doc.setId("id-1");
        doc.setPlantId("plant-1");
        doc.setUserId("user-1");
        doc.setStatus("WATERING");
        doc.setScheduledAt(Instant.parse("2025-11-30T10:00:00Z"));
        doc.setCompletedAt(Instant.parse("2025-12-01T10:00:00Z"));
        doc.setNote("doc-note");
        return doc;
    }

    private CareEvent sampleDomain() {
        CareEvent e = new CareEvent();
        e.setId("id-1");
        e.setPlantId("plant-1");
        e.setUserId("user-1");
        e.setStatus(CareStatus.WATERING);
        e.setScheduledAt(Instant.parse("2025-11-30T10:00:00Z"));
        e.setCompletedAt(Instant.parse("2025-12-01T10:00:00Z"));
        e.setNote("doc-note");
        return e;
    }

    @Test
    void save_shouldMapDomainToDocumentAndBack() {
        CareEvent event = sampleDomain();
        CareEventDocument savedDoc = sampleDoc();

        when(springRepo.save(any(CareEventDocument.class))).thenReturn(savedDoc);

        CareEvent result = adapter.save(event);

        ArgumentCaptor<CareEventDocument> docCaptor = ArgumentCaptor.forClass(CareEventDocument.class);
        verify(springRepo).save(docCaptor.capture());
        CareEventDocument sent = docCaptor.getValue();

        assertThat(sent.getId()).isEqualTo("id-1");
        assertThat(sent.getStatus()).isEqualTo("WATERING");

        assertThat(result.getId()).isEqualTo("id-1");
        assertThat(result.getStatus()).isEqualTo(CareStatus.WATERING);
    }

    @Test
    void findAll_shouldMapAllDocumentsToDomain() {
        when(springRepo.findAll()).thenReturn(List.of(sampleDoc()));

        List<CareEvent> result = adapter.findAll();

        assertThat(result).hasSize(1);
        CareEvent e = result.getFirst();
        assertThat(e.getId()).isEqualTo("id-1");
        assertThat(e.getStatus()).isEqualTo(CareStatus.WATERING);
    }

    @Test
    void findById_shouldMapOptionalDocumentToDomain() {
        when(springRepo.findById("id-1")).thenReturn(Optional.of(sampleDoc()));

        Optional<CareEvent> result = adapter.findById("id-1");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("id-1");
    }

    @Test
    void findByPlantId_shouldDelegateToSpringRepoAndMap() {
        when(springRepo.findByPlantId("plant-1")).thenReturn(List.of(sampleDoc()));

        List<CareEvent> result = adapter.findByPlantId("plant-1");

        assertThat(result).hasSize(1);
        verify(springRepo).findByPlantId("plant-1");
    }

    @Test
    void findByStatus_shouldDelegateToSpringRepoAndMap() {
        when(springRepo.findByStatus("WATERING")).thenReturn(List.of(sampleDoc()));

        List<CareEvent> result = adapter.findByStatus(CareStatus.WATERING);

        assertThat(result).hasSize(1);
        verify(springRepo).findByStatus("WATERING");
    }

    @Test
    void findScheduledBetween_shouldUseCustomQueryAndMap() {
        Instant from = Instant.parse("2025-11-01T00:00:00Z");
        Instant to = Instant.parse("2025-12-01T00:00:00Z");
        when(springRepo.findByScheduledAtBetween(from, to)).thenReturn(List.of(sampleDoc()));

        List<CareEvent> result = adapter.findScheduledBetween(from, to);

        assertThat(result).hasSize(1);
        verify(springRepo).findByScheduledAtBetween(from, to);
    }

    @Test
    void deleteById_shouldDelegateToSpringRepo() {
        adapter.deleteById("id-123");
        verify(springRepo).deleteById("id-123");
    }
}
