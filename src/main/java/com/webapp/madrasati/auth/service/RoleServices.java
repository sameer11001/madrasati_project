package com.webapp.madrasati.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.repository.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServices {

    private final RoleRepository roleRepository;

    RoleServices(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
