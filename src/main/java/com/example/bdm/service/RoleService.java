package com.example.bdm.service;

import com.example.bdm.dto.RoleDto;
import com.example.bdm.model.Role;
import com.example.bdm.repository.RoleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to actions on Role entity especially when it comes to interacting with the database
 */
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieve all roles sorted by name from the database and returns a list of the DTO made from the Role instances
     * @return list of RoleDto
     */
    @Transactional
    public List<RoleDto> getAllRolesDTOSortedByName() {
        List<Role> foundRoles = roleRepository.findAll(Sort.by("name"));
        return foundRoles.stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }
}
