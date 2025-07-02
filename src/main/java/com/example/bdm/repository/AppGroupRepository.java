package com.example.bdm.repository;

import com.example.bdm.model.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppGroupRepository extends JpaRepository<AppGroup,Long> {
    List<AppGroup> findByList_Id(Long listId);
}
