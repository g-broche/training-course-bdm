package com.example.bdm.controller;

import com.example.bdm.dto.RequestUserGdprUpdate;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.Role;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:usertest-${random.uuid};DB_CLOSE_DELAY=-1"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Role role;

    @BeforeAll
    void setUpRelationConstraints(){
        role = roleRepository.save(new Role("role 1"));
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getAll_ShouldReturnAllExistingUsers() throws Exception{
        AppUser user1 = userRepository.save(new AppUser(
                "firstName",
                "lastName",
                "test.test@test.test",
                "!testingPass7",
                role

        ));

        AppUser user2 = userRepository.save(new AppUser(
                "firstName2",
                "lastName2",
                "test2.test@test.test",
                "!testingPass7",
                role

        ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].lastName").value("lastName"))
                .andExpect(jsonPath("$.[1].lastName").value("lastName2"));
    }

    @Test
    void getById_GivenValidId_ShouldReturnCorrespondingUser() throws Exception{
        AppUser user1 = userRepository.save(new AppUser(
                "firstName",
                "lastName",
                "test1.test@test.test",
                "!testingPass7",
                role

        ));

        AppUser user2 = userRepository.save(new AppUser(
                "firstName2",
                "lastName2",
                "test2.test@test.test",
                "!testingPass7",
                role

        ));

        mockMvc.perform(get("/api/users/"+user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("lastName2"));
    }
    @Test
    void getById_GivenInvalidId_ShouldReturn404Error() throws Exception{
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().is(404));
    }

    @Test
    void deleteById_GivenValidId_ShouldDeleteCorrespondingUser() throws Exception{
        AppUser userToDelete = userRepository.save(new AppUser(
                "toDelete",
                "toDelete",
                "testdelete.test@test.test",
                "!testingPass7",
                role
        ));
        Long idToDelete = userToDelete.getId();

        mockMvc.perform(delete("/api/users/"+idToDelete))
                .andExpect(status().isNoContent());

        assertThrows(NoSuchElementException.class, () -> userRepository.findById(idToDelete).orElseThrow());
    }

    @Test
    @Transactional
    void getGdprStatusForUserId_ShouldReturnExpectedUserGdprStatus() throws Exception{
        AppUser userWithNoGdpr = new AppUser(
                "notAccepted",
                "lastName",
                "testgdpr1.test@test.test",
                "!testingPass7",
                role
        );
        userWithNoGdpr.setGdpr(false);

        AppUser userWithGdpr = new AppUser(
                "Accepted",
                "lastName2",
                "testgdpr2.test@test.test",
                "!testingPass7",
                role
        );
        userWithGdpr.setGdpr(true);

        userWithNoGdpr = userRepository.save(userWithNoGdpr);
        userWithGdpr = userRepository.save(userWithGdpr);

        mockMvc.perform(get("/api/users/"+userWithNoGdpr.getId()+"/gdpr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasAcceptedGdpr").value(false));

        mockMvc.perform(get("/api/users/"+userWithGdpr.getId()+"/gdpr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasAcceptedGdpr").value(true));
    }

    @Test
    @Transactional
    void setGdprStatusForUserId_WhenChangingFromFalseToTrue_ShouldUpdateAndReturnExpectedMessage() throws Exception{
        AppUser userWithNoGdpr = userRepository .save (new AppUser(
                "notAccepted",
                "lastName",
                "testchangegdpr.test@test.test",
                "!testingPass7",
                role
        ));
        userWithNoGdpr.setGdpr(false);

        RequestUserGdprUpdate changeGdprRequest = new RequestUserGdprUpdate(true);

        mockMvc.perform(post("/api/users/"+userWithNoGdpr.getId()+"/gdpr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeGdprRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User GDPR status has been set to accepted"));
    }

    @Test
    @Transactional
    void setGdprStatusForUserId_WhenChangingFromTrueToFalse_ShouldUpdateAndReturnExpectedMessage() throws Exception{
        AppUser userWithNoGdpr = userRepository .save (new AppUser(
                "notAccepted",
                "lastName",
                "testchangegdpr.test@test.test",
                "!testingPass7",
                role
        ));
        userWithNoGdpr.setGdpr(true);

        RequestUserGdprUpdate changeGdprRequest = new RequestUserGdprUpdate(false);

        mockMvc.perform(post("/api/users/"+userWithNoGdpr.getId()+"/gdpr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeGdprRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User GDPR status has been set to denied"));
    }
}