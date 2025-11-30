package com.example.plantslibrary.adapters.out.pdf;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.domain.model.SunlightLevel;
import com.example.plantslibrary.domain.model.TemperatureComfort;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit-testy dla CareEventExcelAdapter.
 */
@ExtendWith(MockitoExtension.class)
class CareEventExcelAdapterTest {

    @Mock
    CareEventRepositoryPort careEventRepositoryPort;

    @Mock
    PlantRepositoryPort plantRepositoryPort;

    @InjectMocks
    CareEventExcelAdapter adapter;

    private Plant samplePlant() {
        Plant p = new Plant();
        p.setId("plant-1");
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

    private CareEvent fullEvent() {
        CareEvent e = new CareEvent();
        e.setId("event-1");
        e.setPlantId("plant-1");
        e.setUserId("user-1");
        e.setScheduledAt(Instant.parse("2025-11-30T10:00:00Z"));
        e.setCompletedAt(Instant.parse("2025-11-30T12:00:00Z"));
        e.setStatus(CareStatus.PLANNED);
        e.setNote("Remember to water");
        return e;
    }

    @Test
    void generateCareEventsExcel_shouldCreateWorkbookWithHeaderAndRow() throws Exception {
        // given
        CareEvent event = fullEvent();
        when(careEventRepositoryPort.findByStatus(CareStatus.PLANNED))
                .thenReturn(List.of(event));
        when(plantRepositoryPort.findAll())
                .thenReturn(List.of(samplePlant()));

        // when
        byte[] excelBytes = adapter.generateCareEventsExcel();

        // then
        assertThat(excelBytes).isNotNull();
        assertThat(excelBytes.length).isGreaterThan(0);

        // odczytujemy workbook z bajtów
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheetAt(0);

            // nagłówek
            Row header = sheet.getRow(0);
            assertThat(header.getCell(0).getStringCellValue()).isEqualTo("Plant");
            assertThat(header.getCell(1).getStringCellValue()).isEqualTo("User");
            assertThat(header.getCell(2).getStringCellValue()).isEqualTo("Scheduled at");
            assertThat(header.getCell(3).getStringCellValue()).isEqualTo("Completed at");
            assertThat(header.getCell(4).getStringCellValue()).isEqualTo("Status");
            assertThat(header.getCell(5).getStringCellValue()).isEqualTo("Note");

            // pierwszy wiersz z danymi
            Row row = sheet.getRow(1);
            assertThat(row).isNotNull();
            assertThat(row.getCell(0).getStringCellValue()).isEqualTo("Monstera");
            assertThat(row.getCell(1).getStringCellValue()).isEqualTo("user-1");
            assertThat(row.getCell(4).getStringCellValue()).isEqualTo("PLANNED");
            assertThat(row.getCell(5).getStringCellValue()).isEqualTo("Remember to water");

            // daty jako ISO_INSTANT (tak jak w adapterze)
            assertThat(row.getCell(2).getStringCellValue())
                    .isEqualTo(event.getScheduledAt().toString());
            assertThat(row.getCell(3).getStringCellValue())
                    .isEqualTo(event.getCompletedAt().toString());
        }
    }

    @Test
    void generateCareEventsExcel_shouldHandleMissingPlantAndNullFields() throws Exception {
        // given – event bez plant w mapie, część pól null
        CareEvent event = new CareEvent();
        event.setId("event-2");
        event.setPlantId("unknown-plant");
        event.setUserId(null);
        event.setScheduledAt(Instant.parse("2025-11-30T10:00:00Z"));
        event.setCompletedAt(null);
        event.setStatus(CareStatus.PLANNED);
        event.setNote(null);

        when(careEventRepositoryPort.findByStatus(CareStatus.PLANNED))
                .thenReturn(List.of(event));
        when(plantRepositoryPort.findAll())
                .thenReturn(Collections.emptyList());

        // when
        byte[] excelBytes = adapter.generateCareEventsExcel();

        // then
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(1);

            // brak plant -> wpisuje się plant Id
            assertThat(row.getCell(0).getStringCellValue()).isEqualTo("unknown-plant");
            // null userId / completedAt / note -> puste stringi
            assertThat(row.getCell(1).getStringCellValue()).isEqualTo("");
            assertThat(row.getCell(3).getStringCellValue()).isEqualTo("");
            assertThat(row.getCell(5).getStringCellValue()).isEqualTo("");
            // status zawsze jako name()
            assertThat(row.getCell(4).getStringCellValue()).isEqualTo("PLANNED");
        }
    }
}
