package com.example.physical_activity_project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.physical_activity_project.model.UserRoutine;

public interface IUserRoutineRepository extends JpaRepository<UserRoutine, Long> {

    List<UserRoutine> findByUser_Id(Long userId);

    List<UserRoutine> findByRoutine_Id(Long routineId);
}
