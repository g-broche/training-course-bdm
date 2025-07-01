package com.example.bdm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdm.controller.Error.NotFoundStudentException;
import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.mapper.StudentMapper;
import com.example.bdm.model.Student;
import com.example.bdm.service.StudentService;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
  private final StudentService service;
  private final StudentMapper mapper;

  public StudentController(StudentService service, StudentMapper mapper){
    this.service = service;
    this.mapper = mapper;
  }
  
  //Create a student
  @PostMapping
  public ResponseEntity<AppStudentDto> createStudent( @Valid @RequestBody AppStudentDto dto){
    Student student = mapper.toEntity(dto);
    Student saved = service.save(student);    
    return ResponseEntity.ok(mapper.toDTO(saved));
  }

  //Get Student by id
  @GetMapping("/{id}")
  public ResponseEntity<AppStudentDto> getStudent(@PathVariable Long id){
    AppStudentDto studentDto = service.findById(id).map(mapper::toDTO).orElseThrow(() -> new NotFoundStudentException(id));
    return ResponseEntity.ok(studentDto);
  }

  //Get all Students
  @GetMapping
  public List<Student> getAllStudents() {
    return service.findAll();
  }

  //Update Student by id
  @PutMapping("/{id}")
    public ResponseEntity<AppStudentDto> update(@PathVariable Long id, @RequestBody AppStudentDto dto) {
        Student updated = service.findById(id).map(student -> {
            mapper.updateEntity(student, dto);
            return service.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    //Delete Student By id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
      service.deleteById(id);
      return ResponseEntity.noContent().build();
    }
  
}
