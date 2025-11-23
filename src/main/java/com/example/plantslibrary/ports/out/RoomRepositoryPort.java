package com.example.plantslibrary.ports.out;

import com.example.plantslibrary.domain.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryPort {
    Room save(Room room);
    Optional<Room> findById(String id);
    List<Room> findAll();
    void deleteById(String id);
}

