package com.example.bdm.model;

import com.example.bdm.model.enums.Gender;
import com.example.bdm.model.enums.Profile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "has_dwwm", nullable = false)
    private boolean hasDwwm;

    @Column(name = "french_skill", nullable = true)
    private int frenchSkill;

    @Column(name = "tech_skill", nullable = true)
    private int techSkill;

    @ManyToMany(mappedBy = "students")
    private List<AppList> lists = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "profile", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHasDwwm() {
        return hasDwwm;
    }

    public void setHasDwwm(boolean hasDwwm) {
        this.hasDwwm = hasDwwm;
    }

    public int getFrenchSkill() {
        return frenchSkill;
    }

    public void setFrenchSkill(int frenchSkill) {
        this.frenchSkill = frenchSkill;
    }

    public int getTechSkill() {
        return techSkill;
    }

    public void setTechSkill(int techSkill) {
        this.techSkill = techSkill;
    }

    public List<AppList> getLists() {
        return lists;
    }

    public void setLists(List<AppList> lists) {
        this.lists = lists;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
