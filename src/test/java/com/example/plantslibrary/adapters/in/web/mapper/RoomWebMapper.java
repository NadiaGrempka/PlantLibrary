package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.in.web.dto.RoomCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.RoomResponse;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort.CreateRoomCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomWebMapperTest {

    @Test
    void toCommand_shouldMapFromRoomCreateRequest() {
        // given
        RoomCreateRequest request = new RoomCreateRequest();
        request.setName("Salon");
        request.setTargetTemperature(22.0);
        request.setTargetHumidity(45);
        request.setWindowOrientation("SOUTH");

        // when
        CreateRoomCommand command = RoomWebMapper.toCommand(request);

        // then
        assertNotNull(command);
        assertEquals("Salon", command.name());
        assertEquals(22.0, command.targetTemperature());
        assertEquals(45, command.targetHumidity());
        assertEquals("SOUTH", command.windowOrientation());
    }

    @Test
    void toResponse_shouldMapFromRoomDomain() {
        // given
        Room room = new Room();
        room.setId("room-1");
        room.setName("Sypialnia");
        room.setTargetTemperature(20.0);
        room.setTargetHumidity(50);
        room.setWindowOrientation("EAST");

        // when
        RoomResponse response = RoomWebMapper.toResponse(room);

        // then
        assertNotNull(response);
        assertEquals("room-1", response.getId());
        assertEquals("Sypialnia", response.getName());
        assertEquals(20.0, response.getTargetTemperature());
        assertEquals(50, response.getTargetHumidity());
        assertEquals("EAST", response.getWindowOrientation());
    }
}
