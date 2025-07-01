package com.example.bdm.mapper;

import org.springframework.stereotype.Component;

import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.model.Student;

@Component
public class StudentMapper {
  public AppStudentDto toDTO(Student student){
    if (student == null) return null;


    AppStudentDto dto = new AppStudentDto();
    dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setAge(student.getAge());
        dto.setHasDwwm(student.isHasDwwm());
        dto.setFrenchSkill(student.getFrenchSkill());
        dto.setTechSkill(student.getTechSkill());
        dto.setProfile(student.getProfile());
        dto.setGender(student.getGender());
        return dto;
  }

  public Student toEntity(AppStudentDto dto){
    if (dto == null) return null;

        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setAge(dto.getAge());
        student.setHasDwwm(dto.isHasDwwm());
        student.setFrenchSkill(dto.getFrenchSkill());
        student.setTechSkill(dto.getTechSkill());
        student.setProfile(dto.getProfile());
        student.setGender(dto.getGender());
        return student;
  }

  public void updateEntity(Student student, AppStudentDto dto) {
        student.setFirstName(dto.getFirstName());
        student.setAge(dto.getAge());
        student.setHasDwwm(dto.isHasDwwm());
        student.setFrenchSkill(dto.getFrenchSkill());
        student.setTechSkill(dto.getTechSkill());
        student.setProfile(dto.getProfile());
        student.setGender(dto.getGender());
    }
}
