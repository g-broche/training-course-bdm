package com.example.bdm.controller;

import com.example.bdm.dto.AppUserDto;
import com.example.bdm.dto.RoleDto;
import com.example.bdm.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService userService) {
        this.roleService = userService;
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<RoleDto> RolesData = roleService.getAllRolesDTOSortedByName();
            return ResponseEntity.ok(RolesData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not retrieve role list due to internal error");
        }
    }
}
