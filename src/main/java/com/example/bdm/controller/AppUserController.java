package com.example.bdm.controller;

import com.example.bdm.dto.AppUserDto;
import com.example.bdm.model.AppUser;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class AppUserController {

    private final UserService userService;

    public AppUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<AppUserDto> UsersData = userService.getAllUserDTOSortedByName();
            return ResponseEntity.ok(UsersData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not retrieve user list due to internal error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            AppUserDto foundUserData = userService.getUserDTOById(id);
            return ResponseEntity.ok(foundUserData);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not retrieve user due to internal error");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        }
    }
}