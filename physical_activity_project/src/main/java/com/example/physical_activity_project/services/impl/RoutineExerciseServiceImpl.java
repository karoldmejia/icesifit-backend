package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import com.example.physical_activity_project.services.IRoutineExerciseService;

@Service
public class RoutineExerciseServiceImpl implements IRoutineExerciseService {

    @Autowired
    private IRoutineExerciseRepository routineExerciseRepository;

    @Override
    public RoutineExercise createRoutineExercise(RoutineExercise routineExercise) {
        return routineExerciseRepository.save(routineExercise);
    }

    @Override
    public RoutineExercise updateRoutineExercise(Long id, RoutineExercise updatedRoutineExercise) {
        Optional<RoutineExercise> optional = routineExerciseRepository.findById(id);

        if (optional.isPresent()) {
            RoutineExercise existing = optional.get();
            existing.setSets(updatedRoutineExercise.getSets());
            existing.setReps(updatedRoutineExercise.getReps());
            existing.setTime(updatedRoutineExercise.getTime());
            existing.setExercise(updatedRoutineExercise.getExercise());
            existing.setRoutine(updatedRoutineExercise.getRoutine());
            return routineExerciseRepository.save(existing);
        } else {
            throw new RuntimeException("RoutineExercise not found with id: " + id);
        }
    }

    @Override
    public void deleteRoutineExercise(Long id) {
        if (routineExerciseRepository.existsById(id)) {
            routineExerciseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete. RoutineExercise not found with id: " + id);
        }
    }

    @Override
    public List<RoutineExercise> getRoutineExercisesByRoutine(Long routineId) {
        return routineExerciseRepository.findByRoutine_Id(routineId);
    }

    @Override
    public List<RoutineExercise> getRoutineExercisesByExercise(Long exerciseId) {
        return routineExerciseRepository.findByExercise_Id(exerciseId);
    }

    @Override
    public List<RoutineExercise> getAllRoutineExercises() {
        return routineExerciseRepository.findAll();
    }
}
