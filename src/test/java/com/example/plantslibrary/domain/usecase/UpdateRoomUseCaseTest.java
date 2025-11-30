package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.exception.RoomNotFoundException;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRoomUseCaseTest {

    @Mock
    RoomRepositoryPort roomRepositoryPort;

    @InjectMocks
    UpdateRoomUseCase useCase;

    @Test
    void updateRoom_shouldUpdateNonNullFieldsAndSave() {
        // given
        Room existing = new Room();
        existing.setId("room-1");
        existing.setName("Old name");
        existing.setTargetTemperature(20.0);
        existing.setTargetHumidity(40);
        existing.setWindowOrientation("EAST");

        when(roomRepositoryPort.findById("room-1")).thenReturn(Optional.of(existing));
        when(roomRepositoryPort.save(any(Room.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateRoomUsePort.UpdateRoomCommand command =
                new UpdateRoomUsePort.UpdateRoomCommand(
                        "room-1",
                        "New name",
                        23.5,
                        50,
                        "SOUTH"
                );

        // when
        Room result = useCase.updateRoom(command);

        // then
        assertEquals("room-1", result.getId());
        assertEquals("New name", result.getName());
        assertEquals(23.5, result.getTargetTemperature());
        assertEquals(50, result.getTargetHumidity());
        assertEquals("SOUTH", result.getWindowOrientation());
    }

    @Test
    void updateRoom_shouldLeaveFieldsUntouchedWhenNullInCommand() {
        // given
        Room existing = new Room();
        existing.setId("room-1");
        existing.setName("Old name");
        existing.setTargetTemperature(20.0);
        existing.setTargetHumidity(40);
        existing.setWindowOrientation("EAST");

        when(roomRepositoryPort.findById("room-1")).thenReturn(Optional.of(existing));
        when(roomRepositoryPort.save(any(Room.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // aktualizujemy tylko nazwę – reszta ma zostać jak była
        UpdateRoomUsePort.UpdateRoomCommand command =
                new UpdateRoomUsePort.UpdateRoomCommand(
                        "room-1",
                        "Only name changed",
                        null,
                        null,
                        null
                );

        // when
        Room result = useCase.updateRoom(command);

        // then
        assertEquals("Only name changed", result.getName());
        assertEquals(20.0, result.getTargetTemperature());
        assertEquals(40, result.getTargetHumidity());
        assertEquals("EAST", result.getWindowOrientation());
    }

    @Test
    void updateRoom_shouldThrowWhenRoomNotFound() {
        // given
        when(roomRepositoryPort.findById("missing")).thenReturn(Optional.empty());

        UpdateRoomUsePort.UpdateRoomCommand command =
                new UpdateRoomUsePort.UpdateRoomCommand(
                        "missing",
                        "whatever",
                        21.0,
                        40,
                        "NORTH"
                );

        // when + then
        assertThrows(RoomNotFoundException.class, () -> useCase.updateRoom(command));
        verify(roomRepositoryPort, never()).save(any());
    }
}
