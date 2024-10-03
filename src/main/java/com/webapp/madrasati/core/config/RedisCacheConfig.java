package com.webapp.madrasati.core.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisCacheConfig {

        @Bean
        public RedisCacheConfiguration cacheConfiguration(ObjectMapper mapper) {
                ObjectMapper myMapper = mapper.copy()
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                .registerModule(
                                                new Hibernate6Module()
                                                                .enable(Hibernate6Module.Feature.FORCE_LAZY_LOADING)
                                                                .enable(Hibernate6Module.Feature.REPLACE_PERSISTENT_COLLECTIONS))
                                .activateDefaultTyping(
                                                BasicPolymorphicTypeValidator.builder()
                                                                .allowIfSubType(Object.class)
                                                                .build(),
                                                ObjectMapper.DefaultTyping.NON_FINAL,
                                                JsonTypeInfo.As.PROPERTY);

                return RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .disableCachingNullValues()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(
                                                                new GenericJackson2JsonRedisSerializer(myMapper)));
        }
}
