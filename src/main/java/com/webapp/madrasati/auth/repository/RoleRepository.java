package com.webapp.madrasati.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.core.repository.BaseRepository;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {

    Optional<Role> findByRoleName(String roleName);
}
