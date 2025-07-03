package com.example.bdm.controller;

import com.example.bdm.dto.*;
import com.example.bdm.exception.*;
import com.example.bdm.model.AppUser;
import com.example.bdm.service.AuthService;
import com.example.bdm.utils.SanitizerUtil;
import com.example.bdm.utils.ValidatorUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final SanitizerUtil sanitizerUtil;
    private final ValidatorUtil validatorUtil;

    public AuthController(AuthService authService, SanitizerUtil sanitizerUtil, ValidatorUtil validatorUtil) {
        this.authService = authService;
        this.sanitizerUtil = sanitizerUtil;
        this.validatorUtil = validatorUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RequestRegister request) {
        try{
            RequestRegister sanitizedRequest = sanitizerUtil.sanitizeRegisterInputs(request);
            List<String> validationErrors = validatorUtil.validateRegisterInputs(sanitizedRequest);
            boolean isRequestInvalid = !validationErrors.isEmpty();
            if (isRequestInvalid){
                ErrorsDto errorsDto = new ErrorsDto(validationErrors);
                return ResponseEntity.badRequest().body(errorsDto);
            } else {
                authService.registerNewUser(sanitizedRequest);
                return ResponseEntity.ok("User registered");
            }
        } catch (InvalidRegistrationInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchRoleException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (UserNotCreatedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            String message = "user was created but an error occured when sending the validation email";
            return ResponseEntity.internalServerError().body(message);
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
            return ResponseEntity.ok("Your account has been successfully validated");
        } catch (RegistrationActivationFailedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("This activation link is invalid");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An unexpected error occured on the server");
        }
    }

}