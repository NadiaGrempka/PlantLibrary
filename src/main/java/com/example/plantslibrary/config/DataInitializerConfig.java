package com.example.plantslibrary.config;

import com.example.plantslibrary.adapters.out.mongo.SpringDataPlantRepository;
import com.example.plantslibrary.adapters.out.mongo.SpringDataRoomRepository;
import com.example.plantslibrary.adapters.out.mongo.SpringDataCareEventRepository;
import com.example.plantslibrary.adapters.out.mongo.document.CareEventDocument;
import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import com.example.plantslibrary.adapters.out.mongo.document.RoomDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner initData(SpringDataRoomRepository roomRepo,
                               SpringDataPlantRepository plantRepo,
                               SpringDataCareEventRepository careEventRepo) {
        return args -> {
            System.out.println(">>> INIT START: rooms=" + roomRepo.count()
                    + ", plants=" + plantRepo.count()
                    + ", events=" + careEventRepo.count());

            if (roomRepo.count() > 0 || plantRepo.count() > 0) {
                System.out.println(">>> INIT: data already present, skipping seeding");
                return;
            }

            // === Pokoje ===
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

            // === Rośliny ===
            PlantDocument monstera = new PlantDocument();
            monstera.setName("Monstera");
            monstera.setHydrationLevel(70);
            monstera.setHumidityLevel(60);
            monstera.setSunlightLevel("MEDIUM");
            monstera.setFertilizerNeeded(false);
            monstera.setCurrentTemperature(22.5);
            monstera.setTemperatureComfort("OK");
            monstera.setRoomId(livingRoom.getId());
            monstera = plantRepo.save(monstera);

            PlantDocument snakePlant = new PlantDocument();
            snakePlant.setName("Sansewieria");
            snakePlant.setHydrationLevel(40);
            snakePlant.setHumidityLevel(35);
            snakePlant.setSunlightLevel("LOW");
            snakePlant.setFertilizerNeeded(true);
            snakePlant.setCurrentTemperature(20.0);
            snakePlant.setTemperatureComfort("OK");
            snakePlant.setRoomId(bedroom.getId());
            snakePlant = plantRepo.save(snakePlant);

            // === Care events ===
            CareEventDocument monsteraWater = new CareEventDocument();
            monsteraWater.setPlantId(monstera.getId());
            monsteraWater.setStatus("WATERING");
            monsteraWater.setCompletedAt(LocalDate.now()
                    .minusDays(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            monsteraWater.setNote("Podlane wieczorem");

            CareEventDocument monsteraFertilize = new CareEventDocument();
            monsteraFertilize.setPlantId(monstera.getId());
            monsteraFertilize.setStatus("FERTILIZING");
            monsteraFertilize.setScheduledAt(LocalDate.now()
                    .minusWeeks(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            monsteraFertilize.setNote("Nawóz uniwersalny");

            CareEventDocument snakeWater = new CareEventDocument();
            snakeWater.setPlantId(snakePlant.getId());
            snakeWater.setStatus("WATERING");
            snakeWater.setCompletedAt(LocalDate.now()
                    .minusDays(3)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            snakeWater.setNote("Mało wody, bo sansewieria");

            careEventRepo.save(monsteraWater);
            careEventRepo.save(monsteraFertilize);
            careEventRepo.save(snakeWater);

            System.out.println(">>> INIT END: rooms=" + roomRepo.count()
                    + ", plants=" + plantRepo.count()
                    + ", events=" + careEventRepo.count());
        };
    }
}
