package com.example.plantslibrary.adapters.out.mongo;

import com.example.plantslibrary.adapters.out.mongo.document.CareEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SpringDataCareEventRepository extends MongoRepository<CareEventDocument, String> {

    List<CareEventDocument> findByPlantId(String plantId);

    List<CareEventDocument> findByStatus(String status);

    @Query("{ 'scheduledAt': { $gte: ?0, $lte: ?1 } }")
    List<CareEventDocument> findByScheduledAtBetween(Instant from, Instant to);
}
