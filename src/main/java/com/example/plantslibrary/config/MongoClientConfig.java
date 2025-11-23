package com.example.plantslibrary.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoClientConfig {

    private final MongoClient mongoClient;

    public MongoClientConfig(@Value("${spring.data.mongodb.uri}") String uri) {
        ConnectionString connectionString = new ConnectionString(uri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder
                        .maxSize(50)
                        .minSize(5)
                        .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                )
                .build();

        this.mongoClient = MongoClients.create(settings);
    }

    @Bean
    public MongoClient mongoClient() {
        return mongoClient;
    }

    @PreDestroy
    public void closeMongoClient() {
        mongoClient.close();
    }
}
