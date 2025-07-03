package com.example.bdm.controller;

import com.example.bdm.dto.AppListDto;
import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.mapper.AppListMapper;
import com.example.bdm.model.AppList;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.AppGroup;
import com.example.bdm.model.Role;
import com.example.bdm.model.Student;
import com.example.bdm.model.enums.Gender;
import com.example.bdm.model.enums.Profile;
import com.example.bdm.service.AppListService;
import com.example.bdm.service.AppGroupService;
import com.example.bdm.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Unit tests for the extended functionality of AppListController
@ExtendWith(MockitoExtension.class)
public class AppListControllerExtendedTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AppListService appListService;

    @Mock
    private AppListMapper appListMapper;

    @Mock
    private AppGroupService appGroupService;

    @Mock
    private StudentService studentService;

    private AppListController appListController;

    private AppList testAppList;
    private AppUser testUser;
    private List<Student> testStudents;
    private AppListDto testAppListDto;
    private Role testRole;
    private AppGroupDto testAppGroupDto;
    private AppStudentDto testAppStudentDto;

    @BeforeAll
    static void setupClass() {
        System.out.println("_______Setting up AppListControllerExtended tests_______");
    }

    @BeforeEach
    void setUp() {
        // Initialize controller with all required services
        appListController = new AppListController(appListService, appListMapper, appGroupService, studentService);

        // Setup MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(appListController).build();
        objectMapper = new ObjectMapper();

        // Create test role
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");

        // Create test user
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setFirstName("testUserName");
        testUser.setLastName("testUserLastName");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setIsActive(true);
        testUser.setGdpr(true);
        testUser.setRole(testRole);

        // Create test student
        Student testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("testStudentName");
        testStudent.setAge(25);
        testStudent.setHasDwwm(true);
        testStudent.setProfile(Profile.CONFIDENT);
        testStudent.setGender(Gender.M);

        // Create test students list
        testStudents = new ArrayList<>();
        testStudents.add(testStudent);

        // Create test AppList
        testAppList = new AppList();
        testAppList.setId(1L);
        testAppList.setName("testAppList1");
        testAppList.setUser(testUser);
        testAppList.setStudents(testStudents);
        testAppList.setCreatedAt(Timestamp.from(Instant.now()));
        testAppList.setEditedAt(Timestamp.from(Instant.now()));

        // Create test AppListDto
        testAppListDto = new AppListDto();
        testAppListDto.setId(1L);
        testAppListDto.setName("testAppList1");
        testAppListDto.setUser(testUser);
        testAppListDto.setStudents(testStudents);
        testAppListDto.setCreatedAt(testAppList.getCreatedAt());
        testAppListDto.setEditedAt(testAppList.getEditedAt());

        // Create test AppGroupDto
        testAppGroupDto = new AppGroupDto();
        testAppGroupDto.setName("testGroup");
        testAppGroupDto.setListId(testAppList);

        // Create test AppStudentDto
        testAppStudentDto = new AppStudentDto();
        testAppStudentDto.setFirstName("newTestStudent");
        testAppStudentDto.setAge(30);
        testAppStudentDto.setHasDwwm(true);
        testAppStudentDto.setFrenchSkill(3);
        testAppStudentDto.setTechSkill(4);
        testAppStudentDto.setProfile(Profile.CONFIDENT);
        testAppStudentDto.setGender(Gender.M);
    }

    @AfterAll
    static void tearDownClass() {
        System.out.println("_______AppListControllerExtended tests completed_______");
    }

    // Test adding groups to a list - Success
    @Test
    @WithMockUser(username = "testUser", password = "pass")
    void testAddGroupsToList_Success() throws Exception {
        // Create test group
        AppGroup testGroup = new AppGroup();
        testGroup.setId(1L);
        testGroup.setName("testGroup");
        testGroup.setList(testAppList);

        // Mock service responses
        when(appListService.findById(1L)).thenReturn(Optional.of(testAppList));
        when(appGroupService.createGroup(any(AppGroupDto.class))).thenReturn(ResponseEntity.ok(testGroup));

        // Create list of group DTOs
        List<AppGroupDto> groupDtos = Collections.singletonList(testAppGroupDto);

        // Perform the request
        mockMvc.perform(post("/api/lists/1/addGroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupDtos)))
            .andExpect(status().isOk());

        // Verify service calls
        verify(appListService, times(1)).findById(1L);
        verify(appGroupService, times(1)).createGroup(any(AppGroupDto.class));
    }

    // Test adding groups to a list - Not Found
    @Test
    @WithMockUser(username = "testUser", password = "pass")
    void testAddGroupsToList_NotFound() throws Exception {
        // Mock service responses
        when(appListService.findById(999L)).thenReturn(Optional.empty());

        // Create list of group DTOs
        List<AppGroupDto> groupDtos = Collections.singletonList(testAppGroupDto);

        // Perform the request
        mockMvc.perform(post("/api/lists/999/addGroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupDtos)))
            .andExpect(status().isNotFound());

        // Verify service calls
        verify(appListService, times(1)).findById(999L);
        verify(appGroupService, never()).createGroup(any(AppGroupDto.class));
    }

    // Test adding a student to a list - Success
    @Test
    @WithMockUser(username = "testUser", password = "pass")
    void testAddStudentToList_Success() throws Exception {
        // Create test student
        Student newStudent = new Student();
        newStudent.setId(2L);
        newStudent.setFirstName("newTestStudent");
        newStudent.setAge(30);
        newStudent.setHasDwwm(true);
        newStudent.setProfile(Profile.CONFIDENT);
        newStudent.setGender(Gender.M);

        // Mock service responses
        when(appListService.findById(1L)).thenReturn(Optional.of(testAppList));
        when(studentService.save(any(Student.class))).thenReturn(newStudent);
        when(appListService.save(any(AppList.class))).thenReturn(testAppList);

        // Perform the request
        mockMvc.perform(post("/api/lists/1/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppStudentDto)))
            .andExpect(status().isOk());

        // Verify service calls
        verify(appListService, times(1)).findById(1L);
        verify(studentService, times(1)).save(any(Student.class));
        verify(appListService, times(1)).save(any(AppList.class));
    }

    // Test adding a student to a list - Not Found
    @Test
    @WithMockUser(username = "testUser", password = "pass")
    void testAddStudentToList_NotFound() throws Exception {
        // Mock service responses
        when(appListService.findById(999L)).thenReturn(Optional.empty());

        // Perform the request
        mockMvc.perform(post("/api/lists/999/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppStudentDto)))
            .andExpect(status().isNotFound());

        // Verify service calls
        verify(appListService, times(1)).findById(999L);
        verify(studentService, never()).save(any(Student.class));
        verify(appListService, never()).save(any(AppList.class));
    }
}
