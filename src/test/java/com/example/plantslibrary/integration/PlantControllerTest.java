package com.example.plantslibrary.integration;

import com.example.plantslibrary.adapters.out.mongo.SpringDataPlantRepository;
import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class PlantControllerTest extends AbstractMongoIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringDataPlantRepository springDataPlantRepository;

    @AfterEach
    void cleanup() {
        springDataPlantRepository.deleteAll();
    }

    // 1) pozytywny scenariusz: create + read
    @Test
    void createPlant_andGetById_shouldReturnCreatedPlant() throws Exception {
        String body = """
        {
          "name": "Monstera-REST",
          "hydrationLevel": 60,
          "humidityLevel": 50,
          "sunlightLevel": "MEDIUM",
          "fertilizerNeeded": false,
          "currentTemperature": 22.5,
          "roomId": "room-xyz"
        }
        """;

        // POST
        String response = mockMvc.perform(post("/api/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String createdId = objectMapper.readTree(response).get("id").asText();

        // GET
        mockMvc.perform(get("/api/plants/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Monstera-REST"));
    }

    // 2) walidacja: nieprawidłowe dane -> 400
    @Test
    void createPlant_withInvalidHydration_shouldReturn400() throws Exception {
        String body = """
        {
          "name": "",
          "hydrationLevel": 200,
          "humidityLevel": -5,
          "sunlightLevel": "MEDIUM",
          "fertilizerNeeded": false,
          "currentTemperature": 22.5,
          "roomId": "room-xyz"
        }
        """;

        mockMvc.perform(post("/api/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // 3) getById dla nieistniejącej rośliny -> 404
    @Test
    void getById_notExisting_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/plants/{id}", "non-existing-id"))
                .andExpect(status().isNotFound());
    }

    // 4) update nieistniejącej rośliny -> 404 (jeśli tak masz w use case)
    @Test
    void updateNonExistingPlant_shouldReturn404() throws Exception {
        String body = """
        {
          "hydrationLevel": 70
        }
        """;

        mockMvc.perform(put("/api/plants/{id}", "non-existing-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    // 5) filtrowanie po roomId + sort + paging
    @Test
    void getPlants_withRoomFilterAndPaging_shouldReturnFilteredAndPaged() throws Exception {
        // setup danych
        springDataPlantRepository.save(new PlantDocument(null,"Aloe",60,40,"LOW",false,20.0,"OK","room-1", null, null, null, null ));
        springDataPlantRepository.save(new PlantDocument(null,"Monstera",60,40,"MEDIUM",false,20.0,"OK","room-1", null, null, null, null));
        springDataPlantRepository.save(new PlantDocument(null,"Ficus",60,40,"HIGH",false,20.0,"OK","room-2", null, null, null, null));

        mockMvc.perform(get("/api/plants")
                        .param("roomId", "room-1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Aloe"))
                .andExpect(jsonPath("$.content[1].name").value("Monstera"));
    }

    // 6) DELETE istniejącej rośliny -> 204 i faktyczne usunięcie
    @Test
    void deleteExistingPlant_shouldReturn204AndRemovePlant() throws Exception {
        // given – zapisujemy roślinę bezpośrednio w repo
        PlantDocument saved = springDataPlantRepository.save(
                new PlantDocument(
                        null,
                        "ToDelete",
                        60,
                        40,
                        "MEDIUM",
                        false,
                        22.0,
                        "OK",
                        "room-1",
                        null,
                        null,
                        null,
                        null
                )
        );

        // when / then
        mockMvc.perform(delete("/api/plants/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // i jeszcze sprawdzimy, że faktycznie zniknęła z bazy
        assertThat(springDataPlantRepository.findById(saved.getId())).isEmpty();
    }

    // 7) DELETE nieistniejącej rośliny -> 404
    @Test
    void deleteNonExistingPlant_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/plants/{id}", "non-existing-id"))
                .andExpect(status().isNotFound());
    }

}
