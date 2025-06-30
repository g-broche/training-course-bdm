package com.example.bdm.dto;

import com.example.bdm.model.Role;
import jakarta.persistence.Column;

public class RoleDto {
    private Long id;
    private String name;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
