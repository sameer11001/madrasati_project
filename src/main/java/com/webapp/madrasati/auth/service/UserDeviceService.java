package com.webapp.madrasati.auth.service;

import com.webapp.madrasati.auth.model.UserDevice;
import com.webapp.madrasati.auth.repository.UserDeviceRepository;
import com.webapp.madrasati.core.error.AlreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDeviceService {
    private final UserDeviceRepository userDeviceRepository;

    public boolean existsByDeviceId(String deviceId) {
        return userDeviceRepository.existsByDeviceId(deviceId);
    }

    public Optional<UserDevice> findByDeviceId(String deviceId) {
        return userDeviceRepository.findByDeviceId(deviceId);
    }

    public UserDevice save(UserDevice userDevice) {
        if (existsByDeviceId(userDevice.getDeviceId())) {
            throw new AlreadyExistException("User Device Already Exist");
        }
        return userDeviceRepository.save(userDevice);
    }

}
