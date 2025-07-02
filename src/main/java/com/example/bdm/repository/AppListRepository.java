package com.example.bdm.repository;

import com.example.bdm.model.AppList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppListRepository extends JpaRepository<AppList, Long> {
   List<AppList> findByName(String name);
   List<AppList> findByUserId(Long userId);
}
