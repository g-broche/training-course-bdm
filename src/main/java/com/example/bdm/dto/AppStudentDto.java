package com.example.bdm.dto;


import com.example.bdm.model.enums.Gender;
import com.example.bdm.model.enums.Profile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppStudentDto {
  private Long id;

  @NotBlank(message = "First Name can't be empty")
  @Size(min = 3, message = "Name need 3 caracteres minimals")
  private String firstName;

  @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    private boolean hasDwwm;

    @NotNull(message = "French skill is required")
    @Min(value = 0, message = "French skill must be >= 0")
    @Max(value = 10, message = "French skill must be <= 10")
    private Integer frenchSkill;

    @NotNull(message = "Tech skill is required")
    @Min(value = 0, message = "Tech skill must be >= 0")
    @Max(value = 10, message = "Tech skill must be <= 10")
    private Integer techSkill;

    @NotNull(message = "Profile is required")
    private Profile profile;

    @NotNull(message = "Gender is required")
    private Gender gender;

  public Long getId() { return id;}
  public void setId(Long id) { this.id = id;}

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) {this.firstName = firstName;}

  public int getAge() { return age;}
  public void setAge(int age) { this.age= age;}

  public boolean isHasDwwm() { return hasDwwm;}
  public void setHasDwwm(boolean hasDwwm) {this.hasDwwm = hasDwwm;}

  public Integer getFrenchSkill() { return frenchSkill; }
  public void setFrenchSkill(Integer frenchSkill) { this.frenchSkill = frenchSkill; }

  public Integer getTechSkill() { return techSkill; }
  public void setTechSkill(Integer techSkill) { this.techSkill = techSkill; }

  public com.example.bdm.model.enums.Profile getProfile() {return profile;}
  public void setProfile(com.example.bdm.model.enums.Profile profile) { this.profile = profile;}

  public Gender getGender() {return gender;}
  public void setGender(Gender gender) {this.gender = gender;}
}
