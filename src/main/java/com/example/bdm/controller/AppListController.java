package com.example.bdm.controller;

import com.example.bdm.model.AppList;
import com.example.bdm.dto.AppListDto;
import com.example.bdm.model.Student;
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

   //Get list with name
   @GetMapping("/searchList")
   public ResponseEntity<List<AppListDto>> getAppListByName(@RequestParam String name) {
	  List<AppList> lists = service.findByName(name);

	  if (lists.isEmpty()) { return ResponseEntity.notFound().build(); }

	  List<AppListDto> dtos = lists.stream()
			  .map(mapper::toDTO)
			  .toList();

	  return ResponseEntity.ok(dtos);
   }

   //Get all students in a list {id}/allStudents
   @GetMapping("/{id}/allStudents")
   public ResponseEntity<List<Student>> getAllStudentsInList(@PathVariable Long id) {
	  return service.findById(id)
			  .map(appList -> ResponseEntity.ok(appList.getStudents()))
			  .orElse(ResponseEntity.notFound().build());
   }

   //Update list name {id}/updateNameList
   @PutMapping("/{id}/updateNameList")
   public ResponseEntity<AppListDto> updateListName(@PathVariable Long id, @RequestBody AppListDto dto) {
	  try {
		 return service.findById(id)
				 .map(existingList -> {
					existingList.setName(dto.getName());
					AppList updatedList = service.save(existingList);
					return ResponseEntity.ok(mapper.toDTO(updatedList));
				 })
				 .orElse(ResponseEntity.notFound().build());
	  } catch (Exception e) {
		 return ResponseEntity.badRequest().build();
	  }
   }

   //TODO: Get all lists for current user
   //TODO: Add student to list {id}/addStudent
   //TODO: Add group(s) to list {id}/addGroups
   //TODO: Delete list deleteList/{id}
}
