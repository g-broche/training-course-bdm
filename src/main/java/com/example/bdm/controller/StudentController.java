package com.example.bdm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.mapper.StudentMapper;
import com.example.bdm.model.Student;
import com.example.bdm.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
  private final StudentService service;
  private final StudentMapper mapper;

  public StudentController(StudentService service, StudentMapper mapper){
    this.service = service;
    this.mapper = mapper;
  }
  
  @PostMapping
  public ResponseEntity<AppStudentDto> createStudent(@RequestBody AppStudentDto dto){
    Student student = mapper.toEntity(dto);
    Student saved = service.save(student);
    return ResponseEntity.ok(mapper.toDTO(saved));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppStudentDto> getStudent(@PathVariable Long id){
    return service.findById(id).map(mapper::toDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Student> getAllStudents() {
    return service.findAll();
  }

  
}
