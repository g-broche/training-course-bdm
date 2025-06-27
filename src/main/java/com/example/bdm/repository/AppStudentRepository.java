package com.example.bdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdm.model.Student;

public interface AppStudentRepository extends JpaRepository<Student, Long> {

}
