package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.UserTrainerAssignment;

public interface IUserTrainerAssignmentRepository extends JpaRepository<UserTrainerAssignment, Long>{
    
}
