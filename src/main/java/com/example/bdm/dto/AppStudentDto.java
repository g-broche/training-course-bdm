package com.example.bdm.dto;


import com.example.bdm.model.enums.Gender;

public class AppStudentDto {
  private Long id;
  private String firstName;
  private int age;
  private boolean hasDwwm;
  private Integer frenchSkill;
  private Integer techSkill;
  private com.example.bdm.model.enums.Profile profile;
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
