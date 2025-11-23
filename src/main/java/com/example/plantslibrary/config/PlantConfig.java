package com.example.plantslibrary.config;

import com.example.plantslibrary.ports.in.CreatePlantUsePort;
import com.example.plantslibrary.ports.in.GetPlantsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdatePlantConditionsUsePort;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import com.example.plantslibrary.domain.usecase.CreatePlantUseCase;
import com.example.plantslibrary.domain.usecase.GetPlantsQueryUseCase;
import com.example.plantslibrary.domain.usecase.UpdatePlantConditionsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlantConfig {

    @Bean
    public CreatePlantUsePort createPlantUsePort(PlantRepositoryPort plantRepositoryPort) {
        return new CreatePlantUseCase(plantRepositoryPort);
    }

    @Bean
    public GetPlantsQueryUsePort getPlantsQueryUsePort(PlantRepositoryPort plantRepositoryPort) {
        return new GetPlantsQueryUseCase(plantRepositoryPort);
    }

    @Bean
    public UpdatePlantConditionsUsePort updatePlantConditionsUsePort(PlantRepositoryPort plantRepositoryPort) {
        return new UpdatePlantConditionsUseCase(plantRepositoryPort);
    }
}
