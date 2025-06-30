package com.example.bdm.controller;

import com.example.bdm.model.AppList;
import com.example.bdm.dto.AppListDto;
import com.example.bdm.service.AppListService;
import com.example.bdm.mapper.AppListMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@CrossOrigin(origins = "*")
public class AppListController {

   private final AppListService service;
   private final AppListMapper mapper;

   public AppListController(AppListService service, AppListMapper mapper) {
	  this.service = service;
	  this.mapper = mapper;
   }

   //Get all lists
   @GetMapping
   public List<AppList> getAllAppLists() {
	  return service.findAll();
   }

   //Create a list
   @PostMapping("/createList")
   public ResponseEntity<AppListDto> createAppList(@RequestBody AppListDto dto){
	  List<AppList> appListList = mapper.toEntity(dto);
	  AppList appList = appListList.get(0);
	  AppList saved = service.save(appList);
	  return ResponseEntity.ok(mapper.toDTO(saved));
   }

   //Get one list with id
   @GetMapping("/{id}")
   public ResponseEntity<AppListDto> getAppListById(@PathVariable Long id) {
	  return service.findById(id)
			  .map(mapper::toDTO)
			  .map(ResponseEntity::ok)
			  .orElse(ResponseEntity.notFound().build());
   }
}
