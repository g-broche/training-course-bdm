package com.example.bdm.mapper;

import com.example.bdm.dto.AppListDto;
import com.example.bdm.model.AppList;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AppListMapper {

   //Entity to DTO
	public AppListDto toDTO(AppList appList) {
	   if (appList == null) return null;

	   AppListDto dto = new AppListDto();
	   dto.setId(appList.getId());
	   dto.setName(appList.getName());
	   dto.setUser(appList.getUser());
	   dto.setStudents(appList.getStudents());
	   dto.setCreatedAt(appList.getCreatedAt());
	   dto.setEditedAt(appList.getEditedAt());
	   return dto;
	}

	//DTO to Entity
	public List<AppList> toEntity(AppListDto dto) {
	   if (dto == null) return null;
	   AppList appList = new AppList();
	   appList.setId(dto.getId());
	   appList.setName(dto.getName());
	   appList.setUser(dto.getUser());
	   appList.setStudents(dto.getStudents());
	   appList.setCreatedAt(dto.getCreatedAt());
	   appList.setEditedAt(dto.getEditedAt());
	   return Collections.singletonList(appList);
	}

	//Existing entities updates with DTO
	public void updateEntity(AppList appList, AppListDto dto) {
	   appList.setName(dto.getName());
	   appList.setUser(dto.getUser());
	   appList.setStudents(dto.getStudents());
	   appList.setCreatedAt(dto.getCreatedAt());
	   appList.setEditedAt(dto.getEditedAt());
	   if (dto.getId() != null) {
	      appList.setId(dto.getId());
	   }
	   if (dto.getCreatedAt() != null) {
	      appList.setCreatedAt(dto.getCreatedAt());
	   }
	}
}
