package com.example.bdm.controller;

import com.example.bdm.model.AppList;
import com.example.bdm.model.Student;
import com.example.bdm.repository.AppListRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class AppListController {

   private final AppListRepository appListRepository;

   public AppListController(AppListRepository appListRepository) {
	   this.appListRepository = appListRepository;
   }

   //Get all lists
   @GetMapping("/api/lists")
   public Iterable<AppList> getAllAppLists() {
	  return appListRepository.findAll();
   }

   //Create a list
   @PostMapping("/createList")
   public ResponseEntity<AppList> createAppList(@RequestBody AppList appList) {
	  AppList savedAppList = appListRepository.save(appList);
	  return ResponseEntity.ok(savedAppList);
   }

   //Get one list with id
   @GetMapping("/{id}")
   public ResponseEntity<AppList> getAppListById(@PathVariable Long id) {
	  return appListRepository.findById(id)
			  .map(ResponseEntity::ok)
			  .orElse(ResponseEntity.notFound().build());
   }

   //Search list by name
   @GetMapping("/searchList?name=name")
   public ResponseEntity<List<AppList>> searchListByName(@RequestParam String name) {
	  List<AppList> lists = appListRepository.findByName(name);
	  return ResponseEntity.ok(lists);
   }

   //Get all students in a list
   @GetMapping("/{id}/allStudents")
   public ResponseEntity<List<Student>> getAllStudentsInList(@PathVariable Long id) {
	  return appListRepository.findById(id)
			  .map(list -> ResponseEntity.ok(list.getStudents()))
			  .orElse(ResponseEntity.notFound().build());
   }

   //Get all lists for current user
   @GetMapping("/getAllMyList")
   public ResponseEntity<List<AppList>> getAllMyLists(@RequestParam Long userId) {
	  List<AppList> userLists = appListRepository.findByUserId(userId);
	  return ResponseEntity.ok(userLists);
   }

   // Update list name
   @PatchMapping("/updateNameList/{id}")
   public ResponseEntity<AppList> updateListName(
		   @PathVariable Long id,
		   @RequestBody String name) {
	  return appListRepository.findById(id)
			  .map(list -> {
				 list.setName(name);
				 AppList updatedList = appListRepository.save(list);
				 return ResponseEntity.ok(updatedList);
			  })
			  .orElse(ResponseEntity.notFound().build());
   }

   // Add student to list
   @PostMapping("/{id}/addStudent")
   public ResponseEntity<AppList> addStudentToList(
		   @PathVariable Long id,
		   @RequestBody Student student) {
	  return appListRepository.findById(id)
			  .map(list -> {
				 list.getStudents().add(student);
				 AppList updatedList = appListRepository.save(list);
				 return ResponseEntity.ok(updatedList);
			  })
			  .orElse(ResponseEntity.notFound().build());
   }

   // Add groups of students
   @PostMapping("/{id}/addGroups")
   public ResponseEntity<AppList> addGroupsToList(
		   @PathVariable Long id,
		   @RequestBody List<Student> students) {
	  return appListRepository.findById(id)
			  .map(list -> {
				 list.getStudents().addAll(students);
				 AppList updatedList = appListRepository.save(list);
				 return ResponseEntity.ok(updatedList);
			  })
			  .orElse(ResponseEntity.notFound().build());
   }

   // Delete list
   @DeleteMapping("/deleteList/{id}")
   public ResponseEntity<Void> deleteList(@PathVariable Long id) {
	  return appListRepository.findById(id)
			  .map(list -> {
				 appListRepository.delete(list);
				 return ResponseEntity.ok().<Void>build();
			  })
			  .orElse(ResponseEntity.notFound().build());
   }

   /*TODO HERE:
 - see AppList.java, AppList.java is to not touch at all
 - get : lists | done
 - post : lists/createList | done
 - get : lists/searchList?name=name | done?
 - get : lists/{id}/allStudents  | todo
 - get : lists/getAllMyList (from user account) | todo
 - patch : lists/updateNameList/{id} | todo
 - post : lists/{id}/addStudent //later
 - post : lists/{id}/addGroups //later
 - delete : lists/deleteList/{id} | todo
 - get : created at | todo
 - get : edited at | todo
 */
}
