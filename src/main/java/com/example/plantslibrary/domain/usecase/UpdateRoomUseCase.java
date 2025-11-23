package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.RoomNotFoundException;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;

public class UpdateRoomUseCase implements UpdateRoomUsePort {

    private final RoomRepositoryPort roomRepositoryPort;

    public UpdateRoomUseCase(RoomRepositoryPort roomRepositoryPort) {
        this.roomRepositoryPort = roomRepositoryPort;
    }

    @Override
    public Room updateRoom(UpdateRoomCommand command) {
        Room room = roomRepositoryPort.findById(command.roomId())
                .orElseThrow(() -> new RoomNotFoundException(command.roomId()));

        if (command.name() != null) {
            room.setName(command.name());
        }
        if (command.targetTemperature() != null) {
            room.setTargetTemperature(command.targetTemperature());
        }
        if (command.targetHumidity() != null) {
            room.setTargetHumidity(command.targetHumidity());
        }
        if (command.windowOrientation() != null) {
            room.setWindowOrientation(command.windowOrientation());
        }

        return roomRepositoryPort.save(room);
    }
}
