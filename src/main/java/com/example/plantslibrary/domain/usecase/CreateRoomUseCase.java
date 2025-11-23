package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;

public class CreateRoomUseCase implements CreateRoomUsePort {

    private final RoomRepositoryPort roomRepositoryPort;

    public CreateRoomUseCase(RoomRepositoryPort roomRepositoryPort) {
        this.roomRepositoryPort = roomRepositoryPort;
    }

    @Override
    public Room createRoom(CreateRoomCommand command) {
        Room room = new Room();
        room.setName(command.name());
        room.setTargetTemperature(command.targetTemperature());
        room.setTargetHumidity(command.targetHumidity());
        room.setWindowOrientation(command.windowOrientation());
        return roomRepositoryPort.save(room);
    }
}
