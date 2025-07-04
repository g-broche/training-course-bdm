package com.example.bdm.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.example.bdm.model.AppList;
import com.example.bdm.repository.AppListRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.repository.AppGroupRepository;

@Service
public class AppGroupService {
    private final AppGroupRepository appGroupRepository;
    private final AppListRepository appListRepository;

    public AppGroupService(AppGroupRepository appGroupRepository, AppListRepository appListRepository){
        this.appGroupRepository = appGroupRepository;
        this.appListRepository = appListRepository;
    }
    public ResponseEntity<List<AppGroup>>getAllGroupFromList(@PathVariable Long list_id){
        return ResponseEntity.ok(appGroupRepository.findByList_Id(list_id));
    }
    public ResponseEntity<AppGroupDto> getGroupDetail(@PathVariable Long id){
        Optional<AppGroup> existingGroup = appGroupRepository.findById(id);
        if(existingGroup.isPresent()){
          return ResponseEntity.ok(new AppGroupDto(existingGroup.get()));
        }
        return ResponseEntity.notFound().build();
    }
    public  ResponseEntity<AppGroupDto> createGroup(HttpServletRequest request, AppGroupDto appGroupDto){
         Optional<AppList> existingList = appListRepository.findById(appGroupDto.getListId());
         if(existingList.isEmpty()){
             return ResponseEntity.notFound().build();
         }
         Long userId =  (Long) request.getAttribute("id");
         if(userId == null){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         if(!existingList.get().getUser().getId().equals(userId)){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
        AppGroup appGroup = new AppGroup();
        appGroup.setName(appGroupDto.getName());
        appGroup.setList(existingList.get());
        appGroup.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        appGroup.setEditedAt(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.ok(new AppGroupDto(appGroupRepository.save(appGroup)));
    }
     public ResponseEntity<AppGroupDto> updateGroup(HttpServletRequest request,Long groupId, Long listId,AppGroupDto appGroupDto){
         Optional<AppGroup> existingGroup = appGroupRepository.findById(groupId);
         if(existingGroup.isEmpty()){
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }
         if(!existingGroup.get().getList().getId().equals(listId)){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         Long userId =  (Long) request.getAttribute("id");
         if(userId == null){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         if(!existingGroup.get().getList().getUser().getId().equals(userId)){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
          existingGroup.get().setName(appGroupDto.getName());
         AppGroup savedGroup = existingGroup.get();
         return ResponseEntity.ok(new AppGroupDto(savedGroup));
     }
    // public ResponseEntity<?> deleteGroup(Long id){
    //     Optional<AppGroup> existingGroup = appGroupRepository.findById(id);
    //     if(existingGroup.isEmpty()){
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    //     }
    // }

}
