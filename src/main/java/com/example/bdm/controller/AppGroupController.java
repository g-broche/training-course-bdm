package com.example.bdm.controller;

import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.service.AppGroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<AppGroup> getGroupDetail(@PathVariable Long id){
        return appGroupService.getGroupDetail(id);
    }
    @PostMapping("/")
    public ResponseEntity<AppGroup> createGroup(@Valid @RequestBody AppGroupDto appGroupDto){
        return appGroupService.createGroup(appGroupDto);
    }
    // @PostMapping("/")
    // public ResponseEntity<List<AppGroup>> createGroups(@Valid @RequestBody List<AppGroupDto> appGroupDto){
    //     return appGroupService.createGroups(appGroupDto);
    // }
    // @PatchMapping("/id")
    // public ResponseEntity<AppGroup> updateGroup(@PathVariable Long id,@Valid @RequestBody AppGroupDto appGroupDto) {
    //     return appGroupService.updateGroup(id, appGroupDto);
    // }
    // @DeleteMapping("/id")
    // public ResponseEntity<AppGroup> deleteGroup(@PathVariable Long id){
    //     return appGroupService.deleteGroup(id);
    // }
}
