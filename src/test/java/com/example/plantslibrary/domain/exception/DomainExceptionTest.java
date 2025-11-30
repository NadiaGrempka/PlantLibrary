package com.example.plantslibrary.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionTest {

    @Test
    void plantNotFoundException_shouldExtendDomainException_andExposeMessage() {
        PlantNotFoundException ex = new PlantNotFoundException("plant-123");

        assertThat(ex)
                .isInstanceOf(PlantNotFoundException.class)
                .hasMessageContaining("plant-123");
    }

}
