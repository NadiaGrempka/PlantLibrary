package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.RoomDocument;
import com.example.plantslibrary.domain.model.Room;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;
import com.mongodb.MongoException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Repository  // 👈 dzięki temu Spring widzi beana typu RoomRepositoryPort
public class RoomRepositoryAdapter implements RoomRepositoryPort {

    private final SpringDataRoomRepository springDataRoomRepository;

    public RoomRepositoryAdapter(SpringDataRoomRepository springDataRoomRepository) {
        this.springDataRoomRepository = springDataRoomRepository;
    }

    @Override
    public Room save(Room room) {
        return withRetry(() -> {
            RoomDocument doc = toDocument(room);
            if (doc.getCreatedAt() == null) {
                doc.setCreatedAt(Instant.now());
            }
            RoomDocument saved = springDataRoomRepository.save(doc);
            return toDomain(saved);
        });
    }

    @Override
    public Optional<Room> findById(String id) {
        return withRetry(() -> springDataRoomRepository.findById(id))
                .map(this::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return withRetry(springDataRoomRepository::findAll).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        withRetry(() -> {
            springDataRoomRepository.deleteById(id);
            return null;
        });
    }


    private <T> T withRetry(Supplier<T> supplier) {
        int maxAttempts = 3;
        MongoException last = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return supplier.get();
            } catch (MongoException ex) {
                last = ex;
                if (attempt == maxAttempts) {
                    throw ex;
                }
                try {
                    Thread.sleep(100L * attempt);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    throw ex;
                }
            }
        }
        throw last;
    }

    private RoomDocument toDocument(Room room) {
        RoomDocument doc = new RoomDocument();
        doc.setId(room.getId());
        doc.setName(room.getName());
        doc.setTargetTemperature(room.getTargetTemperature());
        doc.setTargetHumidity(room.getTargetHumidity());
        doc.setWindowOrientation(room.getWindowOrientation());
        // możesz dodać więcej pól jeśli masz w Room/RoomDocument
        return doc;
    }

    private Room toDomain(RoomDocument doc) {
        Room room = new Room();
        room.setId(doc.getId());
        room.setName(doc.getName());
        room.setTargetTemperature(doc.getTargetTemperature());
        room.setTargetHumidity(doc.getTargetHumidity());
        room.setWindowOrientation(doc.getWindowOrientation());
        return room;
    }
}
