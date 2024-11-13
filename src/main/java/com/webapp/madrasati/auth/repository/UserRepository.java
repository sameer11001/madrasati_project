package com.webapp.madrasati.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.core.repository.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserEmail(String email);

    boolean existsByUserEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.userSchool.id = :schoolId")
    List<UserEntity> getAllUsersBySchoolId(UUID schoolId);
}
