package com.example.bdm.config;

import com.example.bdm.model.Role;
import com.example.bdm.model.enums.AvailableRoles;
import com.example.bdm.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (AvailableRoles availableRole : AvailableRoles.values()) {
            String roleName = availableRole.getDisplayName();
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }
}