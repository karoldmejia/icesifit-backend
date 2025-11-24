package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.repository.IUserRoutineRepository;
import com.example.physical_activity_project.services.IUserRoutineService;

@Service
public class UserRoutineServiceImpl implements IUserRoutineService {

    @Autowired
    private IUserRoutineRepository userRoutineRepository;
    @Autowired
    private IRoutineExerciseRepository routineExerciseRepository;
    @Autowired
    private RoutineExerciseServiceImpl routineExerciseService;

    @Override
    public UserRoutine assignRoutineToUser(UserRoutine userRoutine) {
        // 1. Guardamos la UserRoutine
        UserRoutine savedUserRoutine = userRoutineRepository.save(userRoutine);

        // 2. Traemos los ejercicios de la rutina base
        List<RoutineExercise> baseExercises = routineExerciseRepository.findByRoutine_Id(userRoutine.getRoutine().getId());

        // 3. Creamos copias de los ejercicios para la UserRoutine
        for (RoutineExercise baseExercise : baseExercises) {
            RoutineExercise copy = new RoutineExercise();
            copy.setExercise(baseExercise.getExercise());
            copy.setSets(baseExercise.getSets());
            copy.setReps(baseExercise.getReps());
            copy.setTime(baseExercise.getTime());
            copy.setUserRoutine(savedUserRoutine); // asigna al usuario
            copy.setRoutine(null); // ya no es base
            routineExerciseRepository.save(copy);
        }

        return savedUserRoutine;
    }
    @Override
    public UserRoutine updateUserRoutine(Long id, UserRoutine updatedUserRoutine) {
        Optional<UserRoutine> optional = userRoutineRepository.findById(id);

        if (optional.isPresent()) {
            UserRoutine existing = optional.get();
            existing.setAssignmentDate(updatedUserRoutine.getAssignmentDate());
            existing.setStatus(updatedUserRoutine.getStatus());
            existing.setRoutine(updatedUserRoutine.getRoutine());
            existing.setUser(updatedUserRoutine.getUser());
            return userRoutineRepository.save(existing);
        } else {
            throw new RuntimeException("UserRoutine not found with id: " + id);
        }
    }

    @Override
    public void deleteUserRoutine(Long id) {
        if (userRoutineRepository.existsById(id)) {
            userRoutineRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete. UserRoutine not found with id: " + id);
        }
    }

    @Override
    public List<UserRoutine> getUserRoutinesByUser(Long userId) {
        return userRoutineRepository.findByUser_Id(userId);
    }

    @Override
    public List<UserRoutine> getUserRoutinesByRoutine(Long routineId) {
        return userRoutineRepository.findByRoutine_Id(routineId);
    }

    @Override
    public List<UserRoutine> getAllUserRoutines() {
        return userRoutineRepository.findAll();
    }

    @Override
    public UserRoutine getUserRoutineById(Long id) {
        return userRoutineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserRoutine not found with id: " + id));
    }

}
