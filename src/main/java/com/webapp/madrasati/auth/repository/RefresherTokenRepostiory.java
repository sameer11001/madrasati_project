package com.webapp.madrasati.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.core.repository.BaseRepository;

@Repository
public interface RefresherTokenRepostiory extends BaseRepository<RefresherToken, UUID> {
    Optional<RefresherToken> findByToken(String token);

    boolean existsByToken(String token);

    boolean existsByUser(UserEntity user);

    boolean existsByDeviceId(String deviceId);

    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RefresherToken rt WHERE rt.user.userEmail = :email")
    boolean existsByUserEmail(@Param("email") String email);
}
