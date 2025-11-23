package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.GetRoomsQueryUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;

import java.util.List;
import java.util.Optional;

public class GetRoomsQueryUseCase implements GetRoomsQueryUsePort {

    private final RoomRepositoryPort roomRepositoryPort;

    public GetRoomsQueryUseCase(RoomRepositoryPort roomRepositoryPort) {
        this.roomRepositoryPort = roomRepositoryPort;
    }

    @Override
    public List<Room> getAll() {
        return roomRepositoryPort.findAll();
    }

    @Override
    public Optional<Room> getById(String id) {
        return roomRepositoryPort.findById(id);
    }

    @Override
    public void deleteById(String id) {
        roomRepositoryPort.deleteById(id);
    }
}
