package com.example.plantslibrary.ports.out;

/**
 * Port for generating binary reports (PDF / Excel) for plants and care events.
 */
public interface RaportGeneratorPort {
    /**
     * Generates a PDF report of available plants with basic information.
     *
     * @return PDF bytes ready to send as HTTP response body
     */
    byte[] generatePlantsPdf();

    /**
     * Generates an Excel report of care events.
     *
     * @return XLSX bytes ready to send as HTTP response body
     */
    byte[] generateCareEventsExcel();
}
