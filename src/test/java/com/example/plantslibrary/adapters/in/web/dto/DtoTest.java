package com.example.plantslibrary.adapters.in.web.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DtoTest {

    @Test
    void plantCreateRequest_gettersAndSetters_shouldWork() {
        PlantCreateRequest req = new PlantCreateRequest();
        req.setName("Monstera");
        req.setRoomId("room-1");
        req.setHydrationLevel(70);

        assertThat(req.getName()).isEqualTo("Monstera");
        assertThat(req.getRoomId()).isEqualTo("room-1");
        assertThat(req.getHydrationLevel()).isEqualTo(70);
    }

    // analogicznie dopisz 1–2 linijkowe testy dla pozostałych DTO:
    // RoomCreateRequest, PlantResponse itd.
}
