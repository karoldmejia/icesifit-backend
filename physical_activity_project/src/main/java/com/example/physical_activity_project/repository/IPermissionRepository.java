package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPermissionRepository extends JpaRepository<Permission, Long>{
}
