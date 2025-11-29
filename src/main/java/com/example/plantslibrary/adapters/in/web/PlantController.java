package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.adapters.in.web.dto.PlantCreateRequest;
import com.example.plantslibrary.adapters.in.web.dto.PlantNameDto;
import com.example.plantslibrary.adapters.in.web.dto.PlantResponse;
import com.example.plantslibrary.adapters.in.web.dto.UpdatePlantConditionsRequest;
import com.example.plantslibrary.adapters.in.web.mapper.PlantMongoMapper;
import com.example.plantslibrary.adapters.in.web.mapper.PlantWebMapper;
import com.example.plantslibrary.adapters.out.mongo.SpringDataPlantRepository;
import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.in.GetPlantsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD operations for {@link Plant} resources.
 * <p>
 * All endpoints are prefixed with {@code /api/plants}.
 */
@RestController
@RequestMapping("/api/plants")
public class PlantController {
    private final CreatePlantUsePort createPlantUsePort;
    private final GetPlantsQueryUsePort getPlantsQueryUsePort;
    private final UpdatePlantConditionsUsePort updatePlantConditionsUsePort;
    private final SpringDataPlantRepository springDataPlantRepository;

    /**
     * Creates new instance of {@link PlantController}.
     *
     * @param createPlantUsePort           use case for creating plants
     * @param getPlantsQueryUsePort       use case for reading plants
     * @param updatePlantConditionsUsePort use case for updating plant conditions
     * @param springDataPlantRepository   Spring Data repository used for paging queries
     */
    public PlantController(CreatePlantUsePort createPlantUsePort,
                           GetPlantsQueryUsePort getPlantsQueryUsePort,
                           UpdatePlantConditionsUsePort updatePlantConditionsUsePort, SpringDataPlantRepository springDataPlantRepository) {
        this.createPlantUsePort = createPlantUsePort;
        this.getPlantsQueryUsePort = getPlantsQueryUsePort;
        this.updatePlantConditionsUsePort = updatePlantConditionsUsePort;
        this.springDataPlantRepository = springDataPlantRepository;
    }

    /**
     * Creates a new plant.
     *
     * @param request validated payload with plant data
     * @return 201 Created with created {@link PlantResponse}
     */
    @PostMapping
    public ResponseEntity<PlantResponse> create(@Valid @RequestBody PlantCreateRequest request) {
        Plant plant = createPlantUsePort.createPlant(PlantWebMapper.toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PlantWebMapper.toResponse(plant));
    }


    //Todo
    /**
     * Returns paginated list of plants, optionally filtered by room.
     *
     * @param roomId   optional room identifier used to filter plants;
     *                 when {@code null} all plants are returned
     * @param pageable pagination and sorting configuration
     * @return page of {@link PlantResponse} objects
     */
    @GetMapping
    public ResponseEntity<Page<PlantResponse>> getAll(
            @RequestParam(required = false) String roomId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        Page<Plant> page;

        if (roomId == null) {
            page = springDataPlantRepository.findAll(pageable)
                    .map(PlantMongoMapper::toDomain);
        } else {
            page = springDataPlantRepository.findByRoomId(roomId, pageable)
                    .map(PlantMongoMapper::toDomain);
        }

        Page<PlantResponse> responsePage = page.map(PlantWebMapper::toResponse);

        return ResponseEntity.ok(responsePage);
    }


    /**
     * Returns a single plant by id.
     *
     * @param id plant identifier
     * @return 200 OK with {@link PlantResponse}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlantResponse> getById(@PathVariable String id) {
        Plant plant = getPlantsQueryUsePort
                .getById(id);

        return ResponseEntity.ok(PlantWebMapper.toResponse(plant));
    }


    /**
     * Partially updates plant conditions.
     *
     * @param id      plant identifier
     * @param request payload with new conditions
     * @return 200 OK with updated {@link PlantResponse}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlantResponse> updateConditions(
            @PathVariable String id,
            @Valid @RequestBody UpdatePlantConditionsRequest request
    ) {
        UpdatePlantConditionsUsePort.UpdateConditionsCommand command = new UpdatePlantConditionsUsePort.UpdateConditionsCommand(
                id,
                request.getHydrationLevel(),
                request.getHumidityLevel(),
                request.getCurrentTemperature(),
                request.getFertilizerNeeded()
        );

        Plant updated = updatePlantConditionsUsePort.updateConditions(command);
        return ResponseEntity.ok(PlantWebMapper.toResponse(updated));
    }

    /**
     * Deletes plant by identifier.
     *
     * @param id plant identifier
     * @return 204 No Content when deletion succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        getPlantsQueryUsePort.getById(id);
        getPlantsQueryUsePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns lightweight list of plant names for a given room.
     *
     * @param roomId identifier of the room
     * @return list of id + name projections
     */
    @GetMapping("/names")
    public List<PlantNameDto> getPlantNamesByRoom(@RequestParam String roomId) {
        return springDataPlantRepository.findNamesByRoomId(roomId).stream()
                .map(p -> new PlantNameDto(p.getId(), p.getName()))
                .toList();
    }

}
