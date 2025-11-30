package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.ports.out.RaportGeneratorPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RaportGeneratorPort raportGeneratorPort;

    @Test
    void plantsPdf_shouldReturnPdfWithProperHeaders() throws Exception {
        byte[] pdfBytes = "PDF-DATA".getBytes(StandardCharsets.UTF_8);
        when(raportGeneratorPort.generatePlantsPdf()).thenReturn(pdfBytes);

        mockMvc.perform(get("/api/reports/plants/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=plants-report.pdf"
                ))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfBytes));
    }

    @Test
    void careEventsExcel_shouldReturnExcelWithProperHeaders() throws Exception {
        byte[] excelBytes = new byte[] {1, 2, 3};
        when(raportGeneratorPort.generateCareEventsExcel()).thenReturn(excelBytes);

        mockMvc.perform(get("/api/reports/care-events/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=care-events.xlsx"
                ))
                .andExpect(content().contentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .andExpect(content().bytes(excelBytes));
    }
}
