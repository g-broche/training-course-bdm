package com.example.bdm.dto;

import com.example.bdm.model.AppUser;
import com.example.bdm.model.Student;

import java.sql.Timestamp;
import java.util.List;

//DTO - Data Transfer Object
public class AppListDto {
   private Long id;
   private String name;
   private AppUser user;
   private List<Student> students;
   private Timestamp createdAt;
   private Timestamp editedAt;

   public Long getId() {return id;}
   public void setId(Long id) {this.id = id;}

   public String getName() {return name;}
   public void setName(String name) {this.name = name;}

   public AppUser getUser() {return user;}
   public void setUser(AppUser user) {this.user = user;}

   public List<Student> getStudents() {return students;}
   public void setStudents(List<Student> students) {this.students = students;}

   public Timestamp getCreatedAt() {return createdAt;}
   public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}

   public Timestamp getEditedAt() {return editedAt;}
   public void setEditedAt(Timestamp editedAt) {this.editedAt = editedAt;}
}
