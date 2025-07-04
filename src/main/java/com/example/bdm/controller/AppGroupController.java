package com.example.bdm.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.service.AppGroupService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/group")
public class AppGroupController {
    private final AppGroupService appGroupService;

    public AppGroupController(AppGroupService appGroupService){
        this.appGroupService = appGroupService;

    }
    @GetMapping("/fromList/{list_id}")
    public ResponseEntity<List<AppGroup>> getAllGroupFromList(@PathVariable Long list_id){
        return appGroupService.getAllGroupFromList(list_id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppGroupDto> getGroupDetail(@PathVariable Long id){
        return appGroupService.getGroupDetail(id);
    }
    @PostMapping("/")
    public ResponseEntity<AppGroupDto> createGroup(HttpServletRequest request, @Valid @RequestBody AppGroupDto appGroupDto){
        return appGroupService.createGroup(request,appGroupDto);
    }
     @PatchMapping("/{groupId}/fromList/{listId}")
     public ResponseEntity<AppGroupDto> updateGroup(HttpServletRequest request,@PathVariable Long groupId,@PathVariable Long listId,@Valid @RequestBody AppGroupDto appGroupDto) {
         return appGroupService.updateGroup(request,groupId, listId, appGroupDto);
     }
    // @DeleteMapping("/id")
    // public ResponseEntity<AppGroup> deleteGroup(@PathVariable Long id){
    //     return appGroupService.deleteGroup(id);
    // }
}
