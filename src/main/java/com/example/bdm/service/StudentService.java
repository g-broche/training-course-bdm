package com.example.bdm.service;

import java.util.List;
import java.util.Optional;

import com.example.bdm.model.Student;
import com.example.bdm.repository.AppStudentRepository;
import com.example.bdm.utils.StudentType;

public class StudentService implements StudentType {

  private final AppStudentRepository repository;

  public StudentService(AppStudentRepository repository){
    this.repository = repository;
  }
   @Override
   public Student save(Student student){
    return repository.save(student);
   }

   @Override
   public Optional<Student> findById(Long id){
    return repository.findById(id);
   }

   @Override
   public List<Student> findAll() {
    return repository.findAll();
   }

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

    @Override
    public void deleteById(Long id){
      repository.deleteById(id);
    }
}
