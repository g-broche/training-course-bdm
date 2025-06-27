package com.example.bdm.service;

import com.example.bdm.model.AppGroup;
import com.example.bdm.repository.AppGroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AppGroupService {
    private final AppGroupRepository appGroupRepository;
    public AppGroupService(AppGroupRepository appGroupRepository){
        this.appGroupRepository = appGroupRepository;
    }
    public List<AppGroup>getAllGroupFromList(@PathVariable Long id){
        return appGroupRepository.findByList_Id(id);
    }
    public AppGroup getGroupDetail(@PathVariable Long id){
        AppGroup existingGroup =  appGroupRepository.findById(id);
    }
}
