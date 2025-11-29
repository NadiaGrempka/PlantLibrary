package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.ports.out.RaportGeneratorPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing endpoints for library reports (plants / care events).
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final RaportGeneratorPort reportGeneratorPort;

    public ReportController(RaportGeneratorPort reportGeneratorPort) {
        this.reportGeneratorPort = reportGeneratorPort;
    }

    @GetMapping("/plants/pdf")
    public ResponseEntity<byte[]> plantsPdf() {
        byte[] bytes = reportGeneratorPort.generatePlantsPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=plants-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping("/care-events/excel")
    public ResponseEntity<byte[]> careEventsExcel() {
        byte[] bytes = reportGeneratorPort.generateCareEventsExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=care-events.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
}
