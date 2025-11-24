package com.example.plantslibrary.adapters.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "plants")
@NoArgsConstructor
@AllArgsConstructor
public class PlantDocument {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private int hydrationLevel;
    private int humidityLevel;
    private String sunlightLevel;
    private boolean fertilizerNeeded;
    private double currentTemperature;
    private String temperatureComfort;
    @Indexed
    private String roomId;


    @DocumentReference(lazy = true)
    private RoomDocument room;

    private Instant createdAt;
    private List<String> tags;

    // embedded document – profil pielęgnacji
    private CareProfile careProfile;

    public boolean getFertilizerNeeded() {
        return fertilizerNeeded;
    }

    public static class CareProfile {
        private Integer wateringFrequencyDays;
        private Double idealTemperatureMin;
        private Double idealTemperatureMax;
    }
}