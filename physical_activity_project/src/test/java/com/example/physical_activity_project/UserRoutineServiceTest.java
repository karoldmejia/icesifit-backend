package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.repository.IUserRoutineRepository;
import com.example.physical_activity_project.services.impl.UserRoutineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserRoutineServiceTest {

    @Mock
    private IUserRoutineRepository userRoutineRepository;

    @InjectMocks
    private UserRoutineServiceImpl userRoutineService;

    private UserRoutine userRoutine;
    private UserRoutine otherUserRoutine;
    private User user;
    private Routine routine;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        routine = new Routine();
        routine.setId(1L);
        routine.setName("Rutina A");

        userRoutine = new UserRoutine();
        userRoutine.setId(1L);
        userRoutine.setUser(user);
        userRoutine.setRoutine(routine);
        userRoutine.setAssignmentDate(Timestamp.valueOf(LocalDateTime.now()));
        userRoutine.setStatus(true);

        otherUserRoutine = new UserRoutine();
        otherUserRoutine.setId(2L);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Otro Usuario");

        Routine routine2 = new Routine();
        routine2.setId(2L);
        routine2.setName("Rutina B");

        otherUserRoutine.setUser(user2);
        otherUserRoutine.setRoutine(routine2);
        otherUserRoutine.setAssignmentDate(Timestamp.valueOf(LocalDateTime.of(2025, 2, 2, 0, 0)));
        otherUserRoutine.setStatus(false);
    }

    @Test
    void assignRoutineToUser_success() {
        when(userRoutineRepository.save(any(UserRoutine.class))).thenReturn(userRoutine);

        UserRoutine saved = userRoutineService.assignRoutineToUser(userRoutine);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertTrue(saved.getStatus());
        verify(userRoutineRepository, times(1)).save(userRoutine);
    }

    @Test
    void updateUserRoutine_success() {
        UserRoutine updateData = new UserRoutine();
        updateData.setAssignmentDate(Timestamp.valueOf(LocalDateTime.of(2025, 3, 3, 0, 0)));
        updateData.setStatus(false);

        Routine newRoutine = new Routine();
        newRoutine.setId(3L);
        updateData.setRoutine(newRoutine);

        User newUser = new User();
        newUser.setId(3L);
        updateData.setUser(newUser);

        when(userRoutineRepository.findById(1L)).thenReturn(Optional.of(userRoutine));
        when(userRoutineRepository.save(any(UserRoutine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserRoutine updated = userRoutineService.updateUserRoutine(1L, updateData);

        assertNotNull(updated);
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 3, 3, 0, 0)), updated.getAssignmentDate());
        assertFalse(updated.getStatus());
        assertEquals(3L, updated.getRoutine().getId());
        assertEquals(3L, updated.getUser().getId());

        verify(userRoutineRepository, times(1)).findById(1L);
        verify(userRoutineRepository, times(1)).save(any(UserRoutine.class));
    }

    @Test
    void updateUserRoutine_notFound_throwsException() {
        UserRoutine updateData = new UserRoutine();
        when(userRoutineRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userRoutineService.updateUserRoutine(999L, updateData)
        );

        assertTrue(ex.getMessage().contains("UserRoutine not found with id"));
        verify(userRoutineRepository, times(1)).findById(999L);
        verify(userRoutineRepository, never()).save(any());
    }

    @Test
    void deleteUserRoutine_success() {
        when(userRoutineRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRoutineRepository).deleteById(1L);

        userRoutineService.deleteUserRoutine(1L);

        verify(userRoutineRepository, times(1)).existsById(1L);
        verify(userRoutineRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserRoutine_notFound_throwsException() {
        when(userRoutineRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userRoutineService.deleteUserRoutine(999L)
        );

        assertTrue(ex.getMessage().contains("Cannot delete. UserRoutine not found with id"));
        verify(userRoutineRepository, times(1)).existsById(999L);
        verify(userRoutineRepository, never()).deleteById(anyLong());
    }

    @Test
    void getUserRoutinesByUser_success() {
        List<UserRoutine> list = List.of(userRoutine);
        when(userRoutineRepository.findByUser_Id(1L)).thenReturn(list);

        List<UserRoutine> result = userRoutineService.getUserRoutinesByUser(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUser().getId());
        verify(userRoutineRepository, times(1)).findByUser_Id(1L);
    }

    @Test
    void getUserRoutinesByRoutine_success() {
        List<UserRoutine> list = List.of(userRoutine);
        when(userRoutineRepository.findByRoutine_Id(1L)).thenReturn(list);

        List<UserRoutine> result = userRoutineService.getUserRoutinesByRoutine(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getRoutine().getId());
        verify(userRoutineRepository, times(1)).findByRoutine_Id(1L);
    }

    @Test
    void getAllUserRoutines_success() {
        List<UserRoutine> all = Arrays.asList(userRoutine, otherUserRoutine);
        when(userRoutineRepository.findAll()).thenReturn(all);

        List<UserRoutine> result = userRoutineService.getAllUserRoutines();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRoutineRepository, times(1)).findAll();
    }
}
