package com.example.plantslibrary.adapters.out.pdf;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit-testy dla PdfReportAdapter.
 */
@ExtendWith(MockitoExtension.class)
class PdfReportAdapterTest {

    @Mock
    PlantRepositoryPort plantRepositoryPort;

    @Mock
    CareEventExcelAdapter careEventExcelAdapter;

    @InjectMocks
    PdfReportAdapter adapter;

    private Plant samplePlant() {
        Plant p = new Plant();
        p.setId("p1");
        p.setName("Monstera");
        p.setHydrationLevel(70);
        p.setHumidityLevel(60);
        p.setSunlightLevel(SunlightLevel.MEDIUM);
        p.setFertilizerNeeded(false);
        p.setCurrentTemperature(22.5);
        p.setTemperatureComfort(TemperatureComfort.OK);
        p.setRoomId("room-1");
        return p;
    }

    @Test
    void generatePlantsPdf_shouldCreateNonEmptyPdfAndQueryRepository() {
        // given
        when(plantRepositoryPort.findAll()).thenReturn(List.of(samplePlant()));

        // when
        byte[] pdfBytes = adapter.generatePlantsPdf();

        // then
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);

        // PDF Box zapisuje pliki z nagłówkiem "%PDF"
        String header = new String(pdfBytes, 0, 4, StandardCharsets.US_ASCII);
        assertThat(header).isEqualTo("%PDF");

        verify(plantRepositoryPort).findAll();
        verifyNoMoreInteractions(plantRepositoryPort);
    }

    @Test
    void generateCareEventsExcel_shouldDelegateToExcelAdapter() {
        // given
        byte[] excelBytes = "dummy excel".getBytes(StandardCharsets.UTF_8);
        when(careEventExcelAdapter.generateCareEventsExcel()).thenReturn(excelBytes);

        // when
        byte[] result = adapter.generateCareEventsExcel();

        // then
        assertThat(result).isSameAs(excelBytes);
        verify(careEventExcelAdapter).generateCareEventsExcel();
        verifyNoMoreInteractions(careEventExcelAdapter);
    }
}
