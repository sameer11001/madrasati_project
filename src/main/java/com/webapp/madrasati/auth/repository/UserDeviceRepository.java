package com.webapp.madrasati.auth.repository;

import com.webapp.madrasati.auth.model.UserDevice;
import com.webapp.madrasati.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends BaseRepository<UserDevice, UUID> {
    Optional<UserDevice> findByDeviceId(String deviceId);
    boolean existsByDeviceId(String deviceId);
}
