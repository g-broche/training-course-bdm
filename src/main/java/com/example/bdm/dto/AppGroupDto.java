package com.example.bdm.dto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.model.AppList;

import java.sql.Timestamp;


public class AppGroupDto {
    private Long id;
    private  String name;
    private AppList listId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AppGroupDto(){}

    public AppGroupDto(AppGroup group){
        this.id = group.getId();
        this.name = group.getName();
        this.listId = group.getList();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getEditedAt();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public AppList getListId() {
        return listId;
    }
    public void setListId(AppList listId){
        this.listId = listId;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt){
        this.createdAt = createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt){
        this.updatedAt = updatedAt;
    }
}
