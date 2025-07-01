package com.example.bdm.dto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.model.AppList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;


public class AppGroupDto {
    private Long id;

    @NotBlank(message = "name is required")
    @Size(max = 50)
    private  String name;

    @NotNull(message = "listId is required")
    private Long listId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AppGroupDto(){}

    public AppGroupDto(AppGroup group){
        this.id = group.getId();
        this.name = group.getName();
        this.listId = group.getList().getId();
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
    public Long getListId() {
        return listId;
    }
    public void setListId(Long listId){
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
