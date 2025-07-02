package com.example.bdm.controller;

import com.example.bdm.dto.*;
import com.example.bdm.exception.EmailAlreadyExistsException;
import com.example.bdm.exception.NoSuchRoleException;
import com.example.bdm.exception.RegistrationActivationFailedException;
import com.example.bdm.model.AppUser;
import com.example.bdm.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RequestRegister request) {
        try{
            authService.registerNewUser(request);

            return ResponseEntity.ok("User registered");
        } catch (EmailAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchRoleException e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An unexpected error occurred while processing the request");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin request, HttpServletResponse response) {
        try {
            AppUser user = authService.getUserFromLogin(request);
            ResponseCookie jwtCookie = authService.generateCookieFromUser(user);
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            return ResponseEntity.ok(new AppUserDto(user));
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Login failed due to server error");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie expiredCookie = authService.generateExpiredCookie();
        response.addCookie(expiredCookie);
        return ResponseEntity.ok().body("User disconnected");
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<?> getById(@PathVariable String token) {
        try {
            boolean isSuccess = authService.validateRegistrationToken(token);
            if(!isSuccess){
                throw new RegistrationActivationFailedException();
            }
            return ResponseEntity.ok("User has been validated");
        } catch (RegistrationActivationFailedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No corresponding user found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not retrieve user due to internal error");
        }
    }

}