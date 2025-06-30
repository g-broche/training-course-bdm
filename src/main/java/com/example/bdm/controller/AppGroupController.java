package com.example.bdm.controller;

import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.model.AppGroup;
import com.example.bdm.service.AppGroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("")
    public ResponseEntity<Long> createGroup(@Valid @RequestBody AppGroupDto appGroupDto){
        return appGroupService.createGroup(appGroupDto);
    }
}
