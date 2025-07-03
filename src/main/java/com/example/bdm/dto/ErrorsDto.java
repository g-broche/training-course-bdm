package com.example.bdm.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorsDto {
    private List<String> errors;
    ErrorsDto(){
    }
    public ErrorsDto(List<String> errors){
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String errorMessage){
        this.errors.add(errorMessage);
    }

    public void removeError(int index){
        this.errors.remove(index);
    }
}
