package com.example.bdm.utils;

import com.example.bdm.model.AppList;

import java.util.List;
import java.util.Optional;

public interface AppListType {
   AppList save(AppList appList);
   Optional<AppList> findById(Long id);
   List<AppList> findAll();
   AppList update(AppList appList, Long id);
   void deleteById(Long id);
}
