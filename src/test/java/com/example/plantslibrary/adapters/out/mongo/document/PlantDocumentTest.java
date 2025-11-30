package com.example.plantslibrary.adapters.out.mongo.document;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlantDocumentTest {

    @Test
    void getFertilizerNeeded_shouldReturnFieldValue() {
        PlantDocument doc = new PlantDocument();
        doc.setFertilizerNeeded(true);

        assertThat(doc.getFertilizerNeeded()).isTrue();

        doc.setFertilizerNeeded(false);
        assertThat(doc.getFertilizerNeeded()).isFalse();
    }

    @Test
    void careProfile_innerClass_canBeInstantiated() {
        PlantDocument.CareProfile profile = new PlantDocument.CareProfile();

        assertThat(profile).isNotNull();
    }
}
