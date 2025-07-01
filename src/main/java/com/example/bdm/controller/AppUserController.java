package com.example.bdm.controller;

import com.example.bdm.dto.AppUserDto;
import com.example.bdm.dto.RequestUserGdprUpdate;
import com.example.bdm.dto.ResponseUserGdpr;
import com.example.bdm.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @GetMapping("/{id}/gdpr")
    public ResponseEntity<?> getGdprStatusForUserId(@PathVariable Long id) {
        try {
            ResponseUserGdpr UserGdprStatus = userService.getUserGdpr(id);
            return ResponseEntity.ok(UserGdprStatus);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not retrieve user gdpr status due to internal error");
        }
    }

    // TO DO : Match request param id with token stored info when token will exist
    @PostMapping("/{id}/gdpr")
    public ResponseEntity<?> setGdprStatusForUserId(@PathVariable Long id, @RequestBody RequestUserGdprUpdate requestUserGdprUpdate) {
        try {
            System.out.println(">>> GDPR Request value : "+requestUserGdprUpdate.getHasAcceptedGdpr());
            boolean hasChanged = userService.updateUserGdpr(id, requestUserGdprUpdate.getHasAcceptedGdpr());
            if(!hasChanged){
                return ResponseEntity.ok("User GDPR acceptance status remained the same");
            }
            String responseMessage = requestUserGdprUpdate.getHasAcceptedGdpr()
                    ? "User GDPR status has been set to accepted"
                    : "User GDPR status has been set to denied";
            return ResponseEntity.ok(responseMessage);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not change user gdpr status due to internal error");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        }
    }
}