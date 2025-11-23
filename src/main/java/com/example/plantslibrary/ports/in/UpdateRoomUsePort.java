package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Room;

public interface UpdateRoomUsePort {

    Room updateRoom(UpdateRoomCommand command);

    record UpdateRoomCommand(
            String roomId,
            String name,
            Double targetTemperature,
            Integer targetHumidity,
            String windowOrientation
    ) {}
}
