package com.example.bdm.controller.Error;

public class NotFoundStudentException extends RuntimeException {
  public NotFoundStudentException(Long id){
    super("Student with id :  " + id + " not found");
  }
}
