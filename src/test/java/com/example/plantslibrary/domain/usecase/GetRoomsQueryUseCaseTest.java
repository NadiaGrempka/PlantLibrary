package com.example.plantslibrary.domain.usecase;

import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRoomsQueryUseCaseTest {

    @Mock
    RoomRepositoryPort roomRepositoryPort;

    @InjectMocks
    GetRoomsQueryUseCase useCase;

    @Test
    void getAll_shouldDelegateToRepository() {
        // given
        Room r1 = new Room();
        r1.setId("room-1");
        Room r2 = new Room();
        r2.setId("room-2");

        when(roomRepositoryPort.findAll()).thenReturn(List.of(r1, r2));

        // when
        List<Room> result = useCase.getAll();

        // then
        assertEquals(2, result.size());
        assertEquals("room-1", result.getFirst().getId());
        verify(roomRepositoryPort).findAll();
    }

    @Test
    void getById_shouldReturnRoomFromRepository() {
        // given
        Room room = new Room();
        room.setId("room-1");
        when(roomRepositoryPort.findById("room-1")).thenReturn(Optional.of(room));

        // when
        Optional<Room> result = useCase.getById("room-1");

        // then
        assertTrue(result.isPresent());
        assertEquals("room-1", result.get().getId());
        verify(roomRepositoryPort).findById("room-1");
    }

    @Test
    void deleteById_shouldDelegateToRepository() {
        // when
        useCase.deleteById("room-1");

        // then
        verify(roomRepositoryPort).deleteById("room-1");
    }
}
