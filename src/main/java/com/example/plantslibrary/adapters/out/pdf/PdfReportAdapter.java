package com.example.plantslibrary.adapters.out.pdf;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import com.example.plantslibrary.ports.out.RaportGeneratorPort;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * PDFBox adapter generating a simple report of all plants.
 */
@Component
public class PdfReportAdapter implements RaportGeneratorPort {

    private final PlantRepositoryPort plantRepositoryPort;
    private final CareEventExcelAdapter excelAdapter;

    public PdfReportAdapter(PlantRepositoryPort plantRepositoryPort,
                            CareEventExcelAdapter excelAdapter) {
        this.plantRepositoryPort = plantRepositoryPort;
        this.excelAdapter = excelAdapter;
    }

    @Override
    public byte[] generatePlantsPdf() {
        List<Plant> plants = plantRepositoryPort.findAll();

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = 800;
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Plants report");
            content.endText();

            content.setFont(PDType1Font.HELVETICA, 10);
            y -= 30;

            for (Plant plant : plants) {
                if (y < 50) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 800;
                }
                content.beginText();
                content.newLineAtOffset(50, y);
                String line = String.format(
                        "%s | hydration: %d%% | humidity: %d%% | sunlight: %s | copies: N/A",
                        plant.getName(),
                        plant.getHydrationLevel(),
                        plant.getHumidityLevel(),
                        plant.getSunlightLevel().name()
                );
                content.showText(line);
                content.endText();
                y -= 15;
            }

            content.close();
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate plants PDF report", e);
        }
    }

    @Override
    public byte[] generateCareEventsExcel() {
        // Delegate to Excel adapter
        return excelAdapter.generateCareEventsExcel();
    }
}
