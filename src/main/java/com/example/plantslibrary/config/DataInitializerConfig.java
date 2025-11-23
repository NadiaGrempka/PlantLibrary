package com.example.plantslibrary.config;

import com.example.plantslibrary.adapters.out.mongo.SpringDataPlantRepository;
import com.example.plantslibrary.adapters.out.mongo.SpringDataRoomRepository;
import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.example.plantslibrary.adapters.out.mongo.document.RoomDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner initData(SpringDataRoomRepository roomRepo,
                               SpringDataPlantRepository plantRepo) {
        return args -> {
            if (roomRepo.count() > 0 || plantRepo.count() > 0) {
                return;
            }

            RoomDocument livingRoom = new RoomDocument();
            livingRoom.setName("Salon");
            livingRoom.setTargetTemperature(22.0);
            livingRoom.setTargetHumidity(45);
            livingRoom.setWindowOrientation("SOUTH");
            livingRoom = roomRepo.save(livingRoom);

            RoomDocument bedroom = new RoomDocument();
            bedroom.setName("Sypialnia");
            bedroom.setTargetTemperature(20.0);
            bedroom.setTargetHumidity(50);
            bedroom.setWindowOrientation("EAST");
            bedroom = roomRepo.save(bedroom);

            PlantDocument monstera = new PlantDocument();
            monstera.setName("Monstera");
            monstera.setHydrationLevel(70);
            monstera.setHumidityLevel(60);
            monstera.setSunlightLevel("MEDIUM");
            monstera.setFertilizerNeeded(false);
            monstera.setCurrentTemperature(22.5);
            monstera.setTemperatureComfort("OK");
            monstera.setRoomId(livingRoom.getId());
            plantRepo.save(monstera);

            PlantDocument snakePlant = new PlantDocument();
            snakePlant.setName("Sansewieria");
            snakePlant.setHydrationLevel(40);
            snakePlant.setHumidityLevel(35);
            snakePlant.setSunlightLevel("LOW");
            snakePlant.setFertilizerNeeded(true);
            snakePlant.setCurrentTemperature(20.0);
            snakePlant.setTemperatureComfort("OK");
            snakePlant.setRoomId(bedroom.getId());
            plantRepo.save(snakePlant);
        };
    }
}
