package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.services.IRoutineService;

@Service
public class RoutineServiceImpl implements IRoutineService {

    @Autowired
    private IRoutineRepository routineRepository;

    @Override
    public Routine createRoutine(Routine routine) {
        return routineRepository.save(routine);
    }

    @Override
    public Routine updateRoutine(Long id, Routine updatedRoutine) {
        Optional<Routine> optionalRoutine = routineRepository.findById(id);

        if (optionalRoutine.isPresent()) {
            Routine existingRoutine = optionalRoutine.get();
            existingRoutine.setName(updatedRoutine.getName());
            existingRoutine.setCertified(updatedRoutine.getCertified());
            existingRoutine.setCreationDate(updatedRoutine.getCreationDate());
            existingRoutine.setRoutineExercises(updatedRoutine.getRoutineExercises());
            return routineRepository.save(existingRoutine);
        } else {
            throw new RuntimeException("Routine not found with id: " + id);
        }
    }

    @Override
    public void deleteRoutine(Long id) {
        if (routineRepository.existsById(id)) {
            routineRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete. Routine not found with id: " + id);
        }
    }

    @Override
    public List<Routine> getRoutinesByUser(Long userId) {
        return routineRepository.findByUserRoutines_User_Id(userId);
    }

    @Override
    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }
}
