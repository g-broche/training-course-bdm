package com.example.bdm.controller;

import com.example.bdm.model.AppList;
import com.example.bdm.dto.AppListDto;
import com.example.bdm.dto.AppGroupDto;
import com.example.bdm.dto.AppStudentDto;
import com.example.bdm.model.AppUser;
import com.example.bdm.model.AppGroup;
import com.example.bdm.model.Student;
import com.example.bdm.service.AppListService;
import com.example.bdm.service.AppGroupService;
import com.example.bdm.service.StudentService;
import com.example.bdm.mapper.AppListMapper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/lists")
public class AppListController {

   private final AppListService service;
   private final AppListMapper mapper;
   private final AppGroupService groupService;
   private final StudentService studentService;

   public AppListController(AppListService service, AppListMapper mapper,
							AppGroupService groupService, StudentService studentService) {
	  this.service = service;
	  this.mapper = mapper;
	  this.groupService = groupService;
	  this.studentService = studentService;
   }

   //Get all lists
   @PreAuthorize("hasRole('ADMIN')")
   @GetMapping
   public ResponseEntity<?> getAllAppLists() {
	  try {
		 List<AppList> lists = service.findAll();
		 List<AppListDto> listDtos = lists.stream()
				 .map(mapper::toDTO)
				 .toList();
		 return ResponseEntity.ok(listDtos);
	  } catch (Exception e) {
		 throw new RuntimeException(e);
	  }
   }

   //Create a list
   @PostMapping("/createList")
   public ResponseEntity<?> createAppList(@RequestBody AppListDto dto){
	  try {
		 List<AppList> appListList = mapper.toEntity(dto);
		 AppList appList = appListList.get(0);
		 AppList saved = service.save(appList);
		 return ResponseEntity.ok(mapper.toDTO(saved));
	  } catch (Exception e) {
		 throw new RuntimeException(e);
	  }
   }

   //Get one list with id
   //Response 500
   @GetMapping("/{id}")
   public ResponseEntity<?> getAppListById(@PathVariable Long id) {
	  try {
		 AppListDto foundListData = service.findById(id)
				 .map(mapper::toDTO)
				 .orElseThrow(() -> new NoSuchElementException("List not found"));
		 return ResponseEntity.ok(foundListData);
	  } catch (NoSuchElementException e) {
		 return ResponseEntity.status(404).body("No corresponding list found");
	  } catch (Exception e) {
		 throw new RuntimeException(e);
	  }
   }

   //Get list with name
   //Response 500
   @GetMapping("/searchList")
   public ResponseEntity<?> getAppListByName(@RequestParam String name) {
	  try {
		 List<AppList> lists = service.findByName(name);

		 if (lists.isEmpty()) { return ResponseEntity.notFound().build(); }

		 List<AppListDto> dtos = lists.stream()
				.map(mapper::toDTO)
				.toList();

		 return ResponseEntity.ok(dtos);
	  } catch (Exception e) {
		 throw new RuntimeException(e);
	  }
   }

   //Get all students in a list {id}/allStudents
   @GetMapping("/{id}/allStudents")
   public ResponseEntity<?> getAllStudentsInList(@PathVariable Long id) {
	  try {
		 return service.findById(id)
				.map(appList -> ResponseEntity.ok(appList.getStudents()))
				.orElse(ResponseEntity.notFound().build());
	  } catch (Exception e) {
		 throw new RuntimeException(e);
	  }
   }

   //Update list name {id}/updateNameList
   @PutMapping("/{id}/updateNameList")
   public ResponseEntity<?> updateListName(@PathVariable Long id, @RequestBody AppListDto dto) {
	  try {
		 return service.findById(id).map(existingList -> {
			existingList.setName(dto.getName());
			AppList updatedList = service.save(existingList);
			return ResponseEntity.ok(mapper.toDTO(updatedList));
		 }).orElse(ResponseEntity.notFound().build());
	  } catch (Exception e) {
		 return ResponseEntity.badRequest().body("Error updating list: " + e.getMessage());
	  }
   }

   //Get all lists for current user
   //Response empty
   @GetMapping("/getAllMyList")
   public ResponseEntity<?> getAllMyList() {
	  try {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 //Might need to adjust
		 AppUser currentUser = (AppUser) authentication.getPrincipal();
		 Long currentUserId = currentUser.getId();

		 List<AppList> userLists = service.findByUserId(currentUserId);

		 List<AppListDto> userListDtos = userLists.stream()
				 .map(mapper::toDTO)
				 .toList();

		 return ResponseEntity.ok(userListDtos);
	  } catch (Exception e) {
		 return ResponseEntity.badRequest().body("Error retrieving lists: " + e.getMessage());
	  }
   }

   //Delete list /{id}/deleteList
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteList(@PathVariable Long id) {
	  try {
		 if (service.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		 }
		 service.deleteById(id);
		 return ResponseEntity.noContent().build(); // HTTP 204 No Content
	  } catch (Exception e) {
		 return ResponseEntity.badRequest().body("Error deleting list: " + e.getMessage());
	  }
   }
   //Add group(s) to list {id}/addGroups
   @PostMapping("/{id}/addGroups")
   public ResponseEntity<?> addGroupsToList(HttpServletRequest request, @PathVariable Long id, @RequestBody List<AppGroupDto> groupDtos) {
      try {
         return service.findById(id)
                .map(existingList -> {
                   List<AppGroupDto> createdGroups = new ArrayList<>();

                   for (AppGroupDto dto : groupDtos) {
                      dto.setListId(existingList.getId());
                      ResponseEntity<AppGroupDto> response = groupService.createGroup(request,dto);

                      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                         createdGroups.add(response.getBody());
                      }
                   }

                   return ResponseEntity.ok(createdGroups);
                })
                .orElse(ResponseEntity.notFound().build());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   //Add new student to list {id}/addStudent
   @PostMapping("/{id}/addStudent")
   public ResponseEntity<?> addStudentToList(@PathVariable Long id, @RequestBody AppStudentDto studentDto) {
      try {
         return service.findById(id)
                .map(existingList -> {
                   //Create and save student
                   Student student = new Student();
                   student.setFirstName(studentDto.getFirstName());
                   student.setAge(studentDto.getAge());
                   student.setHasDwwm(studentDto.isHasDwwm());
                   student.setFrenchSkill(studentDto.getFrenchSkill());
                   student.setTechSkill(studentDto.getTechSkill());
                   student.setProfile(studentDto.getProfile());
                   student.setGender(studentDto.getGender());

                   Student savedStudent = studentService.save(student);

                   //Add student to the list
                   existingList.getStudents().add(savedStudent);
                   service.save(existingList);

                   return ResponseEntity.ok(savedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
