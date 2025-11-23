package com.example.plantslibrary.adapters.out.mongo.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "rooms")
public class RoomDocument {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private Double targetTemperature;
    private Integer targetHumidity;
    private String windowOrientation;
    private Boolean hasHumidifier;
    private Instant createdAt;
    private List<String> notes;

}
