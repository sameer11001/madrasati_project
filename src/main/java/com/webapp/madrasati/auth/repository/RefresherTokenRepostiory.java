package com.webapp.madrasati.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.core.repository.BaseRepository;

@Repository
public interface RefresherTokenRepostiory extends BaseRepository<RefresherToken, UUID> {
    Optional<RefresherToken> findByToken(String token);

    boolean existsByToken(String token);

    boolean existsByUser(UserEntity user);
}
