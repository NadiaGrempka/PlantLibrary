package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Room;

public interface CreateRoomUsePort {

    Room createRoom(CreateRoomCommand command);

    record CreateRoomCommand(
            String name,
            Double targetTemperature,
            Integer targetHumidity,
            String windowOrientation
    ) {}
}
