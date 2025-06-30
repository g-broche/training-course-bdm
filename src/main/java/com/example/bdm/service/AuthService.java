package com.example.bdm.service;

import com.example.bdm.dto.RequestLogin;
import com.example.bdm.dto.RequestRegister;
import com.example.bdm.exception.EmailAlreadyExistsException;
import com.example.bdm.exception.NoSuchRoleException;
import com.example.bdm.exception.UserNotCreatedException;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.Role;
import com.example.bdm.model.enums.AvailableRoles;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.repository.RoleRepository;
import com.example.bdm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

/**
 * Service dedicated to actions related to signup, login and other features in the layer
 * of user authentification
 */
@Service
public class AuthService {
    @Autowired
    private Environment environment;

    private AppUserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;

    public AuthService(
            AppUserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public AppUser registerNewUser(RequestRegister registerData){
        if (userRepository.existsByEmail(registerData.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

            AppUser newUser = new AppUser();
            newUser.setFirstName(registerData.getFirstName());
            newUser.setLastName(registerData.getLastName());
            newUser.setEmail(registerData.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerData.getPassword()));

            Role userRole = roleRepository.findByName(AvailableRoles.USER.toString())
                    .orElseThrow( () -> new NoSuchRoleException("The default user role has not been set up"));
            newUser.setRole(userRole);
            userRepository.save(newUser);
            return userRepository.findByEmail(newUser.getEmail())
                    .orElseThrow(UserNotCreatedException::new);
    }

    public AppUser getUserFromLogin(RequestLogin request){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    public ResponseCookie generateCookieFromUser(AppUser loggedUser){
        String token = jwtUtil.generateToken(loggedUser);
        boolean mustBeSecure = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(mustBeSecure)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();
    }
}
