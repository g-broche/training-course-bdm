package com.example.bdm.utils;

import java.util.List;
import java.util.Optional;

import com.example.bdm.model.Student;

public interface StudentType {
  Student save(Student student);
  Optional<Student> findById(Long id);
  List<Student> findAll();
  Student update(Long id, Student student);
  void deleteById(Long id);
}
