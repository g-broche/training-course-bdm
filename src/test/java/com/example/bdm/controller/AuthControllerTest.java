package com.example.bdm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.bdm.config.JwtProperties;
import com.example.bdm.dto.RequestLogin;
import com.example.bdm.dto.RequestRegister;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.Role;
import com.example.bdm.model.enums.AvailableRoles;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:authtest-${random.uuid};DB_CLOSE_DELAY=-1"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    private Role roleAdmin;
    private Role roleUser;

    @BeforeAll
    void setUpUsers(){
        roleAdmin = roleRepository.findByName(AvailableRoles.ADMIN.toString()).orElseThrow();
        roleUser = roleRepository.findByName(AvailableRoles.USER.toString()).orElseThrow();
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void testRegisterUser_GivenValidRequest_CreatesNewUser() throws Exception {
        RequestRegister registerData = new RequestRegister(
                        "John",
                        "Doe",
                        "john.doe@test.test",
                        "TestPassword1"
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerData)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered"));

        AppUser createdUser = userRepository.findByEmail(registerData.getEmail()).orElseThrow();
        assertEquals("John",createdUser.getFirstName() , "Created user name should match request");
        assertEquals("User",createdUser.getRole().getName() , "default role should be 'User'");
        assertTrue(
                passwordEncoder.matches(registerData.getPassword(), createdUser.getPassword()),
                "Hashed password should match clear password"
        );
    }

    @Test
    void testLogin_GivenValidCredential_ReturnsUserAndJwtCookie() throws Exception {
        String rawPassword = "TestPassword1";
        AppUser existingUser = new AppUser(
                "Jane",
                "Doe",
                "jane.doe@test.test",
                passwordEncoder.encode(rawPassword),
                roleUser
        );
        userRepository.save(existingUser);
        RequestLogin loginData = new RequestLogin("jane.doe@test.test", rawPassword);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andReturn();

        // asserts if cookie header is present is response
        String cookieHeader = result.getResponse().getHeader("Set-Cookie");
        assertNotNull(cookieHeader, "Cookie header should be present");

        // gets content of the cookie name "jwt" (name is due to AuthService.generateCookieFromUser)
        String jwt = null;
        for (String cookiePart : cookieHeader.split(";")) {
            if (cookiePart.trim().startsWith("jwt=")) {
                jwt = cookiePart.trim().substring("jwt=".length());
                break;
            }
        }

        // asserts cookie presence and proper formatting
        assertNotNull(jwt, "JWT cookie should be present");
        assertEquals(3, jwt.split("\\.").length, "JWT should have 3 parts");

        // Proceed to assert that token data is what is expected
        String secret = jwtProperties.getSecret();
        byte[] keyBytes = secret.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(jwt);

        Claims claims = jwsClaims.getBody();
        assertEquals("jane.doe@test.test", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}