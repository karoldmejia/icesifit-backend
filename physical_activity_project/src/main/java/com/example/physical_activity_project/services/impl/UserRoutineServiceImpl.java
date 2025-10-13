package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.repository.IUserRoutineRepository;
import com.example.physical_activity_project.services.IUserRoutineService;

@Service
public class UserRoutineServiceImpl implements IUserRoutineService {

    @Autowired
    private IUserRoutineRepository userRoutineRepository;

    @Override
    public UserRoutine assignRoutineToUser(UserRoutine userRoutine) {
        return userRoutineRepository.save(userRoutine);
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
}
