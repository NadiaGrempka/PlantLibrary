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

@RestController
@RequestMapping("/api/plants")
public class PlantController {
    private final CreatePlantUsePort createPlantUsePort;
    private final GetPlantsQueryUsePort getPlantsQueryUsePort;
    private final UpdatePlantConditionsUsePort updatePlantConditionsUsePort;
    private final SpringDataPlantRepository springDataPlantRepository;

    public PlantController(CreatePlantUsePort createPlantUsePort,
                           GetPlantsQueryUsePort getPlantsQueryUsePort,
                           UpdatePlantConditionsUsePort updatePlantConditionsUsePort, SpringDataPlantRepository springDataPlantRepository) {
        this.createPlantUsePort = createPlantUsePort;
        this.getPlantsQueryUsePort = getPlantsQueryUsePort;
        this.updatePlantConditionsUsePort = updatePlantConditionsUsePort;
        this.springDataPlantRepository = springDataPlantRepository;
    }

    @PostMapping
    public ResponseEntity<PlantResponse> create(@Valid @RequestBody PlantCreateRequest request) {
        Plant plant = createPlantUsePort.createPlant(PlantWebMapper.toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PlantWebMapper.toResponse(plant));
    }

    //Todo
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


    @GetMapping("/{id}")
    public ResponseEntity<PlantResponse> getById(@PathVariable String id) {
        Plant plant = getPlantsQueryUsePort
                .getById(id);

        return ResponseEntity.ok(PlantWebMapper.toResponse(plant));
    }



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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        getPlantsQueryUsePort.getById(id);
        getPlantsQueryUsePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/names")
    public List<PlantNameDto> getPlantNamesByRoom(@RequestParam String roomId) {
        return springDataPlantRepository.findNamesByRoomId(roomId).stream()
                .map(p -> new PlantNameDto(p.getId(), p.getName()))
                .toList();
    }

}
