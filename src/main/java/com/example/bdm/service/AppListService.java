package com.example.bdm.service;

import com.example.bdm.model.AppList;
import com.example.bdm.repository.AppListRepository;
import com.example.bdm.utils.AppListType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Intermediary between Controller and Repository
@Service
public class AppListService implements AppListType {

   private final AppListRepository repository;
   public AppListService(AppListRepository repository){
	  this.repository = repository;
   }

   @Override
   public AppList save(AppList appList) {
	  return repository.save(appList);
   }

   @Override
   public Optional<AppList> findById(Long id) {
	  return repository.findById(id);
   }

   @Override
   public List<AppList> findAll() {
	  return repository.findAll();
   }

   @Override
   public List<AppList> findByName(String name) {
	  return repository.findByName(name);
   }

   @Override
   public AppList update(AppList appList, Long id) {
	  return repository.findById(id)
			  .map(existing -> {
				 existing.setName(appList.getName());
				 existing.setUser(appList.getUser());
				 existing.setStudents(appList.getStudents());
				 existing.setCreatedAt(appList.getCreatedAt());
				 existing.setEditedAt(appList.getEditedAt());
				 return repository.save(existing);
			  })
			  .orElseThrow(() -> new RuntimeException("List not found"));
   }

   @Override
   public void deleteById(Long id) {
		repository.deleteById(id);
   }
}
