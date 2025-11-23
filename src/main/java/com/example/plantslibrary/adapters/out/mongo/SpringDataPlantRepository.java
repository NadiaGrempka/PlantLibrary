package com.example.plantslibrary.adapters.out.mongo;


import com.example.plantslibrary.adapters.out.mongo.document.PlantDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SpringDataPlantRepository extends MongoRepository<PlantDocument, String> {

    List<PlantDocument> findByRoomId(String roomId);
    interface PlantNameProjection {
        String getId();
        String getName();
    }

    Page<PlantDocument> findByRoomId(String roomId, Pageable pageable);

    @Query(value = "{ 'roomId': ?0 }", fields = "{ 'id': 1, 'name': 1 }")
    List<PlantNameProjection> findNamesByRoomId(String roomId);

}