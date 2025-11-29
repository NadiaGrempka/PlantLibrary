package com.example.plantslibrary.config;

import com.example.plantslibrary.domain.model.Plant;
import com.example.plantslibrary.ports.out.PlantRepositoryPort;
import graphql.scalars.ExtendedScalars;
import org.dataloader.MappedBatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * GraphQL configuration: custom scalars and DataLoader definition.
 */
@Configuration
public class GraphQLConfig {

    public static final String PLANT_BY_ID_LOADER = "plantByIdLoader";

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder
                .scalar(ExtendedScalars.DateTime);
    }

    @Bean
    public DataLoaderRegistry dataLoaderRegistry(PlantRepositoryPort plantRepositoryPort) {

        MappedBatchLoader<String, Plant> plantBatchLoader = (ids) -> CompletableFuture.supplyAsync(() -> {
            List<Plant> plants = plantRepositoryPort.findAll()
                    .stream()
                    .filter(p -> ids.contains(p.getId()))
                    .toList();
            return plants.stream().collect(Collectors.toMap(Plant::getId, p -> p));
        });

        DataLoader<String, Plant> plantLoader =
                DataLoaderFactory.newMappedDataLoader(plantBatchLoader);

        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register(PLANT_BY_ID_LOADER, plantLoader);
        return registry;
    }
}
