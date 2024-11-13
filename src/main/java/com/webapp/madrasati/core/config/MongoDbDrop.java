package com.webapp.madrasati.core.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MongoDbDrop {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDbDrop(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void dropDatabaseAtStartup() {
        mongoTemplate.getDb().drop();
        LoggerApp.debug("Database dropped successfully at startup in development environment!");
    }
}
