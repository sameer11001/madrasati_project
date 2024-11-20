// package com.webapp.madrasati.core.config;

// import org.bson.UuidRepresentation;
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
// import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
// import org.springframework.data.mongodb.config.EnableMongoAuditing;
// import org.springframework.data.mongodb.core.MongoTemplate;

// import com.mongodb.MongoClientSettings;
// import com.mongodb.client.MongoClient;
// import com.mongodb.client.MongoClients;

// @Configuration
// @EnableMongoAuditing
// @EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class })
// public class MongoConfig extends AbstractMongoClientConfiguration {



//     @Override
//     protected String getDatabaseName() {
//         return "madrasati";
//     }

//     @Bean
//     @Override
//     public MongoClient mongoClient() {
//         MongoClientSettings settings = MongoClientSettings.builder()
//                 .uuidRepresentation(UuidRepresentation.STANDARD)
//                 .applyConnectionString(new com.mongodb.ConnectionString("mongodb://rootuser:rootpassword@mongodb:27017"))
//                 .build();
//         return MongoClients.create(settings);
//     }

//     @Bean
//     public MongoTemplate mongoTemplate() {
//         return new MongoTemplate(mongoClient(), getDatabaseName());
//     }

// }
