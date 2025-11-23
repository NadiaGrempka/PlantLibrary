package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.adapters.in.web.dto.RoomCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.RoomResponse;
import com.example.plantslibrary.adapters.in.web.dto.RoomUpdateRequest;
import com.example.plantslibrary.adapters.in.web.mapper.RoomWebMapper;
import com.example.plantslibrary.domain.exception.RoomNotFoundException;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.in.CreateRoomUsePort;
import com.example.plantslibrary.ports.in.GetRoomsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort.UpdateRoomCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final CreateRoomUsePort createRoomUsePort;
    private final GetRoomsQueryUsePort getRoomsQueryUsePort;
    private final UpdateRoomUsePort updateRoomUsePort;

    public RoomController(CreateRoomUsePort createRoomUsePort,
                          GetRoomsQueryUsePort getRoomsQueryUsePort,
                          UpdateRoomUsePort updateRoomUsePort) {
        this.createRoomUsePort = createRoomUsePort;
        this.getRoomsQueryUsePort = getRoomsQueryUsePort;
        this.updateRoomUsePort = updateRoomUsePort;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomCreateRequest request) {
        Room room = createRoomUsePort.createRoom(RoomWebMapper.toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RoomWebMapper.toResponse(room));
    }

    @GetMapping
    public List<RoomResponse> getAll() {
        return getRoomsQueryUsePort.getAll().stream()
                .map(RoomWebMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable String id) {
        return getRoomsQueryUsePort.getById(id)
                .map(RoomWebMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(
            @PathVariable String id,
            @Valid @RequestBody RoomUpdateRequest request
    ) {
        UpdateRoomCommand command = new UpdateRoomCommand(
                id,
                request.getName(),
                request.getTargetTemperature(),
                request.getTargetHumidity(),
                request.getWindowOrientation()
        );

        Room updated = updateRoomUsePort.updateRoom(command);
        return ResponseEntity.ok(RoomWebMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        getRoomsQueryUsePort.getById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        getRoomsQueryUsePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
