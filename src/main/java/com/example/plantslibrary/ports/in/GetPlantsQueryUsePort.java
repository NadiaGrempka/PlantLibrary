package com.example.plantslibrary.ports.in;

import com.example.plantslibrary.domain.model.Plant;
import java.util.List;
import java.util.Optional;

public interface GetPlantsQueryUsePort {

    List<Plant> getAll();

    List<Plant> getByRoom(String roomId);

    Plant getById(String id);

    void deleteById(String id);
}