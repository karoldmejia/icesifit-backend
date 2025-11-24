package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.repository.IExerciseRepository;
import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.repository.IUserRoutineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import com.example.physical_activity_project.services.IRoutineExerciseService;

@Service
public class RoutineExerciseServiceImpl implements IRoutineExerciseService {

    @Autowired
    private IRoutineExerciseRepository routineExerciseRepository;
    @Autowired
    private IRoutineRepository routineRepository;
    @Autowired
    private IUserRoutineRepository userRoutineRepository;
    @Autowired
    private IExerciseRepository exerciseRepository;

    @Override
    public RoutineExercise createRoutineExercise(RoutineExerciseDTO dto) {
        Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        RoutineExercise re = new RoutineExercise();
        re.setExercise(exercise);
        re.setSets(dto.getSets());
        re.setReps(dto.getReps());
        re.setTime(dto.getTime());

        if (dto.getUserRoutineId() != null) {
            UserRoutine ur = userRoutineRepository.findById(dto.getUserRoutineId())
                    .orElseThrow(() -> new RuntimeException("UserRoutine not found"));
            re.setUserRoutine(ur);
            re.setRoutine(null); // desconectar de rutina base
        } else if (dto.getRoutineId() != null) {
            Routine routine = routineRepository.findById(dto.getRoutineId())
                    .orElseThrow(() -> new RuntimeException("Routine not found"));
            re.setRoutine(routine);
            re.setUserRoutine(null); // desconectar de userRoutine
        } else {
            throw new RuntimeException("Debe especificar routineId o userRoutineId");
        }

        return routineExerciseRepository.save(re);
    }

    @Override
    public RoutineExercise updateRoutineExercise(Long id, RoutineExerciseDTO dto) {
        RoutineExercise existing = routineExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoutineExercise not found"));

        existing.setSets(dto.getSets());
        existing.setReps(dto.getReps());
        existing.setTime(dto.getTime());

        if (dto.getExerciseId() != null) {
            Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));
            existing.setExercise(exercise);
        }

        // Si pertenece a userRoutine, no tocamos la rutina base
        if (dto.getUserRoutineId() != null) {
            UserRoutine ur = userRoutineRepository.findById(dto.getUserRoutineId())
                    .orElseThrow(() -> new RuntimeException("UserRoutine not found"));
            existing.setUserRoutine(ur);
            existing.setRoutine(null); // desconectar de rutina base
        } else if (dto.getRoutineId() != null) {
            Routine routine = routineRepository.findById(dto.getRoutineId())
                    .orElseThrow(() -> new RuntimeException("Routine not found"));
            existing.setRoutine(routine);
            existing.setUserRoutine(null); // desconectar de userRoutine
        }

        return routineExerciseRepository.save(existing);
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
    public List<RoutineExercise> getRoutineExercisesByUserRoutine(Long userRoutineId) {
        return routineExerciseRepository.findByUserRoutine_Id(userRoutineId);
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
