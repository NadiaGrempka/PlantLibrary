package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRoomUseCaseTest {

    @Mock
    RoomRepositoryPort roomRepositoryPort;

    @InjectMocks
    CreateRoomUseCase useCase;

    @Test
    void createRoom_shouldMapFieldsAndSave() {
        // given
        CreateRoomUsePort.CreateRoomCommand command =
                new CreateRoomUsePort.CreateRoomCommand(
                        "Salon",
                        22.0,
                        45,
                        "SOUTH"
                );

        Room saved = new Room();
        saved.setId("room-1");
        saved.setName("Salon");
        saved.setTargetTemperature(22.0);
        saved.setTargetHumidity(45);
        saved.setWindowOrientation("SOUTH");

        when(roomRepositoryPort.save(any(Room.class))).thenReturn(saved);

        // when
        Room result = useCase.createRoom(command);

        // then – sprawdzamy mapping do Room przy wywołaniu save(...)
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepositoryPort).save(captor.capture());
        Room toSave = captor.getValue();

        assertEquals("Salon", toSave.getName());
        assertEquals(22.0, toSave.getTargetTemperature());
        assertEquals(45, toSave.getTargetHumidity());
        assertEquals("SOUTH", toSave.getWindowOrientation());

        // oraz to, co wróciło z use-case
        assertEquals("room-1", result.getId());
    }
}
