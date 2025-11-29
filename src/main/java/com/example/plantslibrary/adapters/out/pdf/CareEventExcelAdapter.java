package com.example.plantslibrary.adapters.out.pdf;

import com.example.plantslibrary.domain.model.CareEvent;
import com.example.plantslibrary.domain.model.CareStatus;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.out.CareEventRepositoryPort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Apache POI adapter generating an Excel report for care events.
 */
@Component
public class CareEventExcelAdapter {

    private final CareEventRepositoryPort careEventRepositoryPort;
    private final PlantRepositoryPort plantRepositoryPort;

    public CareEventExcelAdapter(CareEventRepositoryPort careEventRepositoryPort,
                                 PlantRepositoryPort plantRepositoryPort) {
        this.careEventRepositoryPort = careEventRepositoryPort;
        this.plantRepositoryPort = plantRepositoryPort;
    }

    public byte[] generateCareEventsExcel() {
        List<CareEvent> events = careEventRepositoryPort.findByStatus(CareStatus.PLANNED);
        Map<String, Plant> plantMap = plantRepositoryPort.findAll().stream()
                .collect(Collectors.toMap(Plant::getId, p -> p));

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Care events");
            DateTimeFormatter fmt = DateTimeFormatter.ISO_INSTANT;

            // header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            Row header = sheet.createRow(0);
            String[] headers = {"Plant", "User", "Scheduled at", "Completed at", "Status", "Note"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (CareEvent event : events) {
                Row row = sheet.createRow(rowIdx++);

                Plant plant = plantMap.get(event.getPlantId());
                row.createCell(0).setCellValue(plant != null ? plant.getName() : event.getPlantId());
                row.createCell(1).setCellValue(event.getUserId() != null ? event.getUserId() : "");
                row.createCell(2).setCellValue(fmt.format(event.getScheduledAt()));
                row.createCell(3).setCellValue(event.getCompletedAt() != null ? fmt.format(event.getCompletedAt()) : "");
                row.createCell(4).setCellValue(event.getStatus().name());
                row.createCell(5).setCellValue(event.getNote() != null ? event.getNote() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate care events Excel report", e);
        }
    }
}
