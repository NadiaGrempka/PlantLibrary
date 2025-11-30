package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.adapters.in.web.dto.RoomCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.RoomResponse;
import com.example.plantslibrary.adapters.in.web.dto.RoomUpdateRequest;
import com.example.plantslibrary.domain.exception.RoomNotFoundException;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort;
import com.example.plantslibrary.ports.in.GetRoomsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort.UpdateRoomCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerUnitTest {

    @Mock
    CreateRoomUsePort createRoomUsePort;

    @Mock
    GetRoomsQueryUsePort getRoomsQueryUsePort;

    @Mock
    UpdateRoomUsePort updateRoomUsePort;

    @InjectMocks
    RoomController controller;

    private Room sampleRoom() {
        Room r = new Room();
        r.setId("room-1");
        r.setName("Salon");
        r.setTargetTemperature(22.0);
        r.setTargetHumidity(45);
        r.setWindowOrientation("SOUTH");
        return r;
    }

    // ---------- create() ----------

    @Test
    void create_shouldReturnCreatedRoomResponse() {
        Room persisted = sampleRoom();
        when(createRoomUsePort.createRoom(any())).thenReturn(persisted);

        RoomCreateRequest req = new RoomCreateRequest();
        req.setName("Salon");
        req.setTargetTemperature(22.0);
        req.setTargetHumidity(45);
        req.setWindowOrientation("SOUTH");

        ResponseEntity<RoomResponse> response = controller.create(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("room-1");
        assertThat(response.getBody().getName()).isEqualTo("Salon");

        verify(createRoomUsePort).createRoom(any());
    }

    // ---------- getAll() ----------

    @Test
    void getAll_shouldMapAllRoomsToResponses() {
        when(getRoomsQueryUsePort.getAll()).thenReturn(List.of(sampleRoom()));

        List<RoomResponse> result = controller.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo("room-1");
        verify(getRoomsQueryUsePort).getAll();
    }

    // ---------- getById() ----------

    @Test
    void getById_shouldReturnRoom_whenExists() {
        when(getRoomsQueryUsePort.getById("room-1")).thenReturn(Optional.of(sampleRoom()));

        ResponseEntity<RoomResponse> response = controller.getById("room-1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("room-1");
        verify(getRoomsQueryUsePort).getById("room-1");
    }

    @Test
    void getById_shouldThrowRoomNotFound_whenMissing() {
        when(getRoomsQueryUsePort.getById("missing")).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> controller.getById("missing"));

        verify(getRoomsQueryUsePort).getById("missing");
    }

    // ---------- update() ----------

    @Test
    void update_shouldBuildCommandAndReturnUpdatedRoom() {
        Room updated = sampleRoom();
        updated.setName("Nowy salon");
        when(updateRoomUsePort.updateRoom(any())).thenReturn(updated);

        RoomUpdateRequest req = new RoomUpdateRequest();
        req.setName("Nowy salon");
        req.setTargetTemperature(23.0);
        req.setTargetHumidity(50);
        req.setWindowOrientation("EAST");

        ResponseEntity<RoomResponse> response = controller.update("room-1", req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Nowy salon");

        ArgumentCaptor<UpdateRoomCommand> captor =
                ArgumentCaptor.forClass(UpdateRoomCommand.class);
        verify(updateRoomUsePort).updateRoom(captor.capture());

        UpdateRoomCommand cmd = captor.getValue();
        assertThat(cmd.roomId()).isEqualTo("room-1");
        assertThat(cmd.name()).isEqualTo("Nowy salon");
        assertThat(cmd.targetTemperature()).isEqualTo(23.0);
        assertThat(cmd.targetHumidity()).isEqualTo(50);
        assertThat(cmd.windowOrientation()).isEqualTo("EAST");
    }

    // ---------- delete() ----------

    @Test
    void delete_shouldCallDeleteAndReturnNoContent_whenRoomExists() {
        when(getRoomsQueryUsePort.getById("room-1")).thenReturn(Optional.of(sampleRoom()));
        doNothing().when(getRoomsQueryUsePort).deleteById("room-1");

        ResponseEntity<Void> response = controller.delete("room-1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(getRoomsQueryUsePort).getById("room-1");
        verify(getRoomsQueryUsePort).deleteById("room-1");
    }

    @Test
    void delete_shouldThrowRoomNotFound_whenMissing() {
        when(getRoomsQueryUsePort.getById("missing")).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> controller.delete("missing"));

        verify(getRoomsQueryUsePort).getById("missing");
        verify(getRoomsQueryUsePort, never()).deleteById(any());
    }
}
