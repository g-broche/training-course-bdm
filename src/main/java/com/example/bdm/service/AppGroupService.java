package com.example.bdm.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.repository.AppGroupRepository;

@Service
public class AppGroupService {
    private final AppGroupRepository appGroupRepository;

    public AppGroupService(AppGroupRepository appGroupRepository){
        this.appGroupRepository = appGroupRepository;
    }
    public ResponseEntity<List<AppGroup>>getAllGroupFromList(@PathVariable Long list_id){
        return ResponseEntity.ok(appGroupRepository.findByList_Id(list_id));
    }
    public ResponseEntity<AppGroup> getGroupDetail(@PathVariable Long id){
        Optional<AppGroup> existingGroup = appGroupRepository.findById(id);
        if(existingGroup.isPresent()){
          return ResponseEntity.ok(existingGroup.get());
        }
        return ResponseEntity.notFound().build();
    }
    public  ResponseEntity<AppGroup> createGroup(AppGroupDto appGroupDto){
        // Vérification de si la list existe
//         Optional<AppList> existingList =
        // Vérification de si la list appartient bien à l'user
        // Vérification de si le nom de la liste est déjà utiliser par l'user
        AppGroup appGroup = new AppGroup();
        appGroup.setName(appGroupDto.getName());
        appGroup.setList(appGroupDto.getListId());
        appGroup.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        appGroup.setEditedAt(new Timestamp(System.currentTimeMillis()));

        return ResponseEntity.ok(appGroupRepository.save(appGroup));
    }
    // A terminer
//     public  ResponseEntity<List<AppGroup>> createGroups(List<AppGroupDto> appGroups){
//         // Vérification de si la list existe
// //         Optional<AppList> existingList =
//         // Vérification de si la list appartient bien à l'user
//         // Vérification de si le nom de la liste est déjà utiliser par l'user
//         List<AppGroup> savedGroups = new ArrayList<>();
    //     for(AppGroupDto dto : appGroups) {


    //         AppGroup group = new AppGroup();
    //         group.setName(dto.getName());
    //         group.setList(dto.getListId());
    //         group.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    //         group.setEditedAt(new Timestamp(System.currentTimeMillis()));

    //         AppGroup savedGroup = appGroupRepository.save(group);
    //         savedGroups.add(savedGroup);
    //     }
    // }
    // public ResponseEntity<AppGroup> updateGroup(Long id,AppGroupDto appGroupDto){
    //     Optional<AppGroup> existingGroup = appGroupRepository.findById(id);
    //     if(existingGroup.isEmpty()){
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    //     }
    // }
    // public ResponseEntity<?> deleteGroup(Long id){
    //     Optional<AppGroup> existingGroup = appGroupRepository.findById(id);
    //     if(existingGroup.isEmpty()){
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    //     }
    // }

}
