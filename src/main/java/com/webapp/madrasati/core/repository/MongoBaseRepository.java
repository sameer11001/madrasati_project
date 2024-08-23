package com.webapp.madrasati.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.webapp.madrasati.core.model.BaseCollection;

@NoRepositoryBean
public interface MongoBaseRepository<T extends BaseCollection, ID> extends MongoRepository<T, ID> {

}
