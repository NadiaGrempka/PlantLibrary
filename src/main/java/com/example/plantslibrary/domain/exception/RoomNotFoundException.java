package com.example.plantslibrary.domain.exception;

public class RoomNotFoundException extends RuntimeException {

    private final String roomId;

    public RoomNotFoundException(String roomId) {
        super("Room with id " + roomId + " not found");
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
