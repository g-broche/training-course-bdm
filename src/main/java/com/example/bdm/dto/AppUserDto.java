package com.example.bdm.dto;

import com.example.bdm.model.AppUser;
import java.sql.Timestamp;

import com.example.bdm.model.AppUser;

public class AppUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
    private Timestamp createdAt;
    private boolean gdpr;

    public AppUserDto(){

    }

    public AppUserDto(AppUser user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.isActive = user.getIsActive();
        this.createdAt = user.getCreatedAt();
        this.gdpr = user.getGdpr();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getGdpr() {
        return gdpr;
    }

    public void setGdpr(boolean gdpr) {
        this.gdpr = gdpr;
    }

}
