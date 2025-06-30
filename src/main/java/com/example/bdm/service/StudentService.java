package com.example.bdm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bdm.model.Student;
import com.example.bdm.repository.AppStudentRepository;
import com.example.bdm.utils.StudentType;

@Service
public class StudentService implements StudentType {

  //Student repository
  private final AppStudentRepository repository;
  
  //Student Service
  public StudentService(AppStudentRepository repository){
    this.repository = repository;
  }

  //Create Student
  @Override
  public Student save(Student student){
    return repository.save(student);
  }

  //Get student By id
  @Override
  public Optional<Student> findById(Long id){
    return repository.findById(id);
  }

  //Get all student
  @Override
  public List<Student> findAll() {
    return repository.findAll();
  }

  //Student update by id
  @Override
  public Student update(Long id, Student updatedStudent) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updatedStudent.getFirstName());
                    existing.setAge(updatedStudent.getAge());
                    existing.setHasDwwm(updatedStudent.isHasDwwm());
                    existing.setFrenchSkill(updatedStudent.getFrenchSkill());
                    existing.setTechSkill(updatedStudent.getTechSkill());
                    existing.setProfile(updatedStudent.getProfile());
                    existing.setGender(updatedStudent.getGender());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

  //Student delete
  @Override
  public void deleteById(Long id){
      repository.deleteById(id);
  }
}
