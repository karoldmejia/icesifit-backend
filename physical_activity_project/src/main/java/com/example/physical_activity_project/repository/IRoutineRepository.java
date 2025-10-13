package com.example.physical_activity_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.physical_activity_project.model.Routine;

@Repository
public interface IRoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserRoutines_User_Id(Long userId);
}
