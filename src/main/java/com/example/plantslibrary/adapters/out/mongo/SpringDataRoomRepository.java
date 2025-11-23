package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.RoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataRoomRepository extends MongoRepository<RoomDocument, String> {
}
