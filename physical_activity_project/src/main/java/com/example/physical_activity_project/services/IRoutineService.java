package com.example.physical_activity_project.services;

import java.util.List;
import com.example.physical_activity_project.model.Routine;

public interface IRoutineService {
    Routine createRoutine(Routine routine);
    Routine updateRoutine(Long id, Routine updatedRoutine);
    void deleteRoutine(Long id);
    List<Routine> getRoutinesByUser(Long userId);
    List<Routine> getAllRoutines();
}
