package com.example.plantslibrary.adapters.in.web.mapper;

import com.example.plantslibrary.adapters.in.web.dto.RoomCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.RoomResponse;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort.CreateRoomCommand;

public class RoomWebMapper {

    public static CreateRoomCommand toCommand(RoomCreateRequest request) {
        return new CreateRoomCommand(
                request.getName(),
                request.getTargetTemperature(),
                request.getTargetHumidity(),
                request.getWindowOrientation()
        );
    }

    public static RoomResponse toResponse(Room room) {
        RoomResponse resp = new RoomResponse();
        resp.setId(room.getId());
        resp.setName(room.getName());
        resp.setTargetTemperature(room.getTargetTemperature());
        resp.setTargetHumidity(room.getTargetHumidity());
        resp.setWindowOrientation(room.getWindowOrientation());
        return resp;
    }
}
