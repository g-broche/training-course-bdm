package com.example.bdm.controller;

import com.example.bdm.model.AppGroup;
import com.example.bdm.service.AppGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/group")
public class AppGroupController {
    private final AppGroupService appGroupService;

    public AppGroupController(AppGroupService appGroupService){
        this.appGroupService = appGroupService;

    }
    @GetMapping("/fromList/{id}")
    public ResponseEntity<List<AppGroup>> getAllGroupFromList(@PathVariable Long id){
        return ResponseEntity.ok(appGroupService.getAllGroupFromList(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppGroup> getGroupDetail(@PathVariable Long id){
        return ResponseEntity.ok(appGroupService.getAllGroupFromList(id));
    }
}
