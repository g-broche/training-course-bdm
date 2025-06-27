package com.example.bdm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdm.model.Student;
import com.example.bdm.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
  private final StudentService service;

  public StudentController(StudentService service){
    this.service = service;
  }
  
  @PostMapping
  public ResponseEntity<Student> createStudent(@RequestBody Student student){
    return ResponseEntity.ok(service.save(student));
  }
}
