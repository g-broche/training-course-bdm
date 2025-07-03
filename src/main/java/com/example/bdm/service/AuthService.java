package com.example.bdm.service;

import com.example.bdm.dto.RequestLogin;
import com.example.bdm.dto.RequestRegister;
import com.example.bdm.exception.ActivationTokenGenerationException;
import com.example.bdm.exception.EmailAlreadyExistsException;
import com.example.bdm.exception.NoSuchRoleException;
import com.example.bdm.exception.UserNotCreatedException;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.Role;
import com.example.bdm.model.enums.AvailableRoles;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.repository.RoleRepository;
import com.example.bdm.utils.ActivationTokenUtil;
import com.example.bdm.utils.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

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
    private EmailService emailService;

    public AuthService(
            AppUserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    /**
     * Creates and persists a new user from signup data
     * @param registerData
     * @return
     * @throws MessagingException
     */
    public AppUser registerNewUser(RequestRegister registerData) throws MessagingException {
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
            String activationToken = generateRegistrationToken();
            newUser.setActivationToken(activationToken);
            userRepository.save(newUser);
            AppUser createdUser = userRepository.findByEmail(newUser.getEmail())
                    .orElseThrow(UserNotCreatedException::new);
            emailService.sendRegistrationActivationEmail(createdUser, activationToken);
            return createdUser;
    }

    /**
     * Gets and returns user corresponding to credentials if such a user exists
     * @param request
     * @return
     */
    public AppUser getUserFromLogin(RequestLogin request){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    /**
     * Generates the cookie containing the token representing a logged user
     * @param loggedUser
     * @return cookie
     */
    public ResponseCookie generateCookieFromUser(AppUser loggedUser){
        String token = jwtUtil.generateToken(loggedUser);
//        boolean mustBeSecure = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();
    }

    /**
     * Generates a expired jwt cookie to be send and remove the jwt cookie
     * @return Cookie
     */
    public Cookie generateExpiredCookie() {
//        boolean mustBeSecure = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    /**
     * Given a token, will search for a user having the same activationToken and if
     * such user exists it will be set to active and its existing token set back to null
     * @param token
     * @return boolean
     */
    @Transactional
    public boolean validateRegistrationToken(String token) {
            AppUser user = userRepository.findByActivationToken(token).orElseThrow();
            user.setIsActive(true);
            user.setActivationToken(null);
            userRepository.save(user);

            return true;
    }

    /**
     * Generates a random token to be used to validate registration. Ensures the token
     * don't already exist beforehand
     * @return String
     */
    private String generateRegistrationToken(){
        Set<String> existingTokens = userRepository.findAllNonNullActivationTokens();
        String activationToken = ActivationTokenUtil.generateToken();;
        boolean mustCreateToken = true;
        int maxAttempts = 10;
        int currentAttempt = 1;
        // prevent collision with other users existing token
        while(mustCreateToken && currentAttempt <= maxAttempts){
            if (!existingTokens.contains(activationToken)){
                mustCreateToken = false;
            } else {
                currentAttempt++;
                activationToken = ActivationTokenUtil.generateToken();
            }
        }
        if(mustCreateToken) {
            throw new ActivationTokenGenerationException();
        }
        return activationToken;
    }
}
