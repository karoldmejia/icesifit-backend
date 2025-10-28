package com.example.physical_activity_project.services;

import java.util.List;
import com.example.physical_activity_project.model.UserRoutine;

public interface IUserRoutineService {

    UserRoutine assignRoutineToUser(UserRoutine userRoutine);
    UserRoutine updateUserRoutine(Long id, UserRoutine updatedUserRoutine);
    void deleteUserRoutine(Long id);
    List<UserRoutine> getUserRoutinesByUser(Long userId);
    List<UserRoutine> getUserRoutinesByRoutine(Long routineId);
    List<UserRoutine> getAllUserRoutines();
    UserRoutine getUserRoutineById(Long id);
}
