package com.example.bdm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.mapper.StudentMapper;
import com.example.bdm.model.Student;
import com.example.bdm.model.enums.Gender;
import com.example.bdm.model.enums.Profile;
import com.example.bdm.service.StudentService;

public class StudentControllerTest {
  @Mock
  private StudentService service;

  @Mock
  private StudentMapper mapper;

  @InjectMocks
  private StudentController controller;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

  //Create a student with a valid DTO
  private AppStudentDto createValiDto() {
    AppStudentDto dto = new AppStudentDto();
    dto.setFirstName("Plopi");
    dto.setAge(25);
    dto.setHasDwwm(true);
    dto.setFrenchSkill(3);
    dto.setTechSkill(4);
    dto.setProfile(Profile.RESERVED);
    dto.setGender(Gender.M);
    return dto;
  }

  //Test create student
  @Test
  void createStudent_CreateStudentDto() {
    AppStudentDto inpuDto = createValiDto();
    Student studentEntity = new Student();
    Student saveEntity = new Student();
    AppStudentDto saveDto = createValiDto();
    saveDto.setId(1L);


    when(mapper.toEntity(inpuDto)).thenReturn(studentEntity);
    when(service.save(studentEntity)).thenReturn(saveEntity);
    when(mapper.toDTO(saveEntity)).thenReturn(saveDto);

    ResponseEntity<AppStudentDto> response = controller.createStudent(inpuDto);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(saveDto, response.getBody());
    verify(service).save(studentEntity);
    verify(mapper).toDTO(saveEntity);
  }

  //Find By Id
  @Test
  void getStudent_GetStudentDto() {
    Long id = 1L;
    Student student = new Student();
    AppStudentDto dto = createValiDto();

    when(service.findById(id)).thenReturn(Optional.of(student));
    when(mapper.toDTO(student)).thenReturn(dto);

    ResponseEntity<AppStudentDto> response = controller.getStudent(id);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(dto, response.getBody());
  }

  //Get All Students
  @Test
  void getAllStudents_GetAllStudent() {
    List<Student> students = Arrays.asList(new Student(), new Student());
    when(service.findAll()).thenReturn(students);

    List<Student> result = controller.getAllStudents();

    assertEquals(2, result.size());
    verify(service).findAll();
  }

   //Test student update
   @Test
    void update_ShouldReturnUpdatedDto() {
        Long id = 1L;
        AppStudentDto inputDto = createValiDto();
        Student existing = new Student();
        Student updated = new Student();
        AppStudentDto updatedDto = createValiDto();
        updatedDto.setId(id);

        when(service.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(mapper).updateEntity(existing, inputDto);
        when(service.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(updatedDto);

        ResponseEntity<AppStudentDto> response = controller.update(id, inputDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedDto, response.getBody());
        verify(mapper).updateEntity(existing, inputDto);
    }

    //Test student Not Found
  @Test
    void update_ShouldThrowIfNotFound() {
        Long id = 404L;
        AppStudentDto dto = createValiDto();
        when(service.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> controller.update(id, dto));
    }

    //Test delete student
    @Test
    void deleteStudent_DeleteStudent() {
      Long id = 1L;

      ResponseEntity<Void> response = controller.delete(id);

      assertEquals(204, response.getStatusCode().value());
      verify(service).deleteById(id);
    }

}
