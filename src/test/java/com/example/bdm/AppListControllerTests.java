package com.example.bdm;

import com.example.bdm.controller.AppListController;
import com.example.bdm.dto.AppListDto;
import com.example.bdm.mapper.AppListMapper;
import com.example.bdm.model.AppList;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.Role;
import com.example.bdm.model.Student;
import com.example.bdm.model.enums.Gender;
import com.example.bdm.model.enums.Profile;
import com.example.bdm.service.AppListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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

 //Unit tests for the AppListController
 //These tests use Mockito to mock the service and mapper dependencies.
@ExtendWith(MockitoExtension.class)
public class AppListControllerTests {

   private MockMvc mockMvc;
   private ObjectMapper objectMapper;

   @Mock
   private AppListService appListService;

   @Mock
   private AppListMapper appListMapper;

   @InjectMocks
   private AppListController appListController;

   private AppList testAppList;
   private AppUser testUser;
   private List<Student> testStudents;
   private AppListDto testAppListDto;
   private Role testRole;

   @BeforeAll
   static void setupClass() {
      System.out.println("_______Setting up AppListController tests_______");
   }

   @BeforeEach
   void setUp() {
      //Setup MockMvc
      mockMvc = MockMvcBuilders.standaloneSetup(appListController).build();
      objectMapper = new ObjectMapper();

      //Create test role
      testRole = new Role();
      testRole.setId(1L);
      testRole.setName("USER");

      //Create test user
      testUser = new AppUser();
      testUser.setId(1L);
      testUser.setFirstName("testUserName");
      testUser.setLastName("testUserLastName");
      testUser.setEmail("test@example.com");
      testUser.setPassword("password");
      testUser.setActive(true);
      testUser.setGdpr(true);
      testUser.setRole(testRole);

      //Create test student
      Student testStudent = new Student();
      testStudent.setId(1L);
      testStudent.setFirstName("testStudentName");
      testStudent.setAge(25);
      testStudent.setHasDwwm(true);
      testStudent.setProfile(Profile.CONFIDENT);
      testStudent.setGender(Gender.M);

      //Create test students list
      testStudents = new ArrayList<>();
      testStudents.add(testStudent);

      //Create test AppList
      testAppList = new AppList();
      testAppList.setId(1L);
      testAppList.setName("testAppList1");
      testAppList.setUser(testUser);
      testAppList.setStudents(testStudents);
      testAppList.setCreatedAt(Timestamp.from(Instant.now()));
      testAppList.setEditedAt(Timestamp.from(Instant.now()));

      //Create test AppListDto
      testAppListDto = new AppListDto();
      testAppListDto.setId(1L);
      testAppListDto.setName("testAppList1");
      testAppListDto.setUser(testUser);
      testAppListDto.setStudents(testStudents);
      testAppListDto.setCreatedAt(testAppList.getCreatedAt());
      testAppListDto.setEditedAt(testAppList.getEditedAt());
   }

   //Get all lists
   @Test
   @WithMockUser(username = "testUser", password = "pass")
   void testGetAllAppLists_Success() throws Exception {
      List<AppList> expectedLists = Collections.singletonList(testAppList);
      when(appListService.findAll()).thenReturn(expectedLists);

      //GET request (no body needed)
      mockMvc.perform(get("/api/lists")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      verify(appListService, times(1)).findAll();
   }

   //Create a list
   @Test
   @WithMockUser(username = "testUser", password = "pass")
   void testCreateAppList_Success() throws Exception {
      when(appListMapper.toEntity(any(AppListDto.class))).thenReturn(Collections.singletonList(testAppList));
      when(appListService.save(any(AppList.class))).thenReturn(testAppList);
      when(appListMapper.toDTO(any(AppList.class))).thenReturn(testAppListDto);

      mockMvc.perform(post("/api/lists/createList")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testAppListDto)))
          .andExpect(status().isOk());

      verify(appListMapper, times(1)).toEntity(any(AppListDto.class));
      verify(appListService, times(1)).save(any(AppList.class));
      verify(appListMapper, times(1)).toDTO(any(AppList.class));
   }

   //Get one list with id
   @Test
   @WithMockUser(username = "testUser", password = "pass")
   void testGetAppListById_Success() throws Exception {
      when(appListService.findById(1L)).thenReturn(Optional.of(testAppList));
      when(appListMapper.toDTO(any(AppList.class))).thenReturn(testAppListDto);

      mockMvc.perform(get("/api/lists/1")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      verify(appListService, times(1)).findById(1L);
      verify(appListMapper, times(1)).toDTO(any(AppList.class));
   }

   @Test
   @WithMockUser(username = "testUser", password = "pass")
   void testGetAppListById_NotFound() throws Exception {
      when(appListService.findById(999L)).thenReturn(Optional.empty());

      mockMvc.perform(get("/api/lists/999")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());

      verify(appListService, times(1)).findById(999L);
      verify(appListMapper, never()).toDTO(any(AppList.class));
   }
}
