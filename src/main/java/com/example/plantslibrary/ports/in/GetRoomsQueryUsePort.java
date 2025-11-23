package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Room;

import java.util.List;
import java.util.Optional;

public interface GetRoomsQueryUsePort {

    List<Room> getAll();

    Optional<Room> getById(String id);

    void deleteById(String id);
}
