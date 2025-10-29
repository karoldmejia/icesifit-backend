package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Schedule;
import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.repository.IScheduleRepository;
import com.example.physical_activity_project.repository.ISpaceRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceImplTest {

    @Mock
    private IScheduleRepository scheduleRepository;

    @Mock
    private ISpaceRepository spaceRepository;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Space space;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        space = new Space();
        space.setId(1L);
        space.setName("Gimnasio Central");

        schedule = new Schedule();
        schedule.setId(100L);
        schedule.setDayOfWeek("Lunes");
        schedule.setStartTime(Timestamp.valueOf(LocalDateTime.of(2025, 10, 29, 8, 0)));
        schedule.setEndTime(Timestamp.valueOf(LocalDateTime.of(2025, 10, 29, 10, 0)));
        schedule.setSpace(space);
    }

    @Test
    void shouldCreateScheduleSuccessfully() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.createSchedule(1L, schedule);

        assertNotNull(result);
        assertEquals("Lunes", result.getDayOfWeek());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
        verify(notificationService, times(1))
                .sendNotificationToAll(contains("Gimnasio Central"), eq("SCHEDULE_CREATED"), eq(100));
    }

    @Test
    void shouldThrowWhenSpaceNotFoundOnCreate() {
        when(spaceRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                scheduleService.createSchedule(999L, schedule)
        );

        assertEquals("Space not found", ex.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllSchedules() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<Schedule> result = scheduleService.getAllSchedules();

        assertEquals(1, result.size());
        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnSchedulesBySpace() {
        when(scheduleRepository.findBySpaceId(1L)).thenReturn(List.of(schedule));

        List<Schedule> result = scheduleService.getSchedulesBySpace(1L);

        assertEquals(1, result.size());
        verify(scheduleRepository, times(1)).findBySpaceId(1L);
    }

    @Test
    void shouldUpdateScheduleSuccessfully() {
        Schedule updated = new Schedule();
        updated.setDayOfWeek("Martes");
        updated.setStartTime(Timestamp.valueOf(LocalDateTime.of(2025, 10, 29, 9, 0)));
        updated.setEndTime(Timestamp.valueOf(LocalDateTime.of(2025, 10, 29, 11, 0)));

        when(scheduleRepository.findById(100L)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.updateSchedule(100L, updated);

        assertEquals("Martes", result.getDayOfWeek());
        verify(scheduleRepository, times(1)).save(schedule);
        verify(notificationService, times(1))
                .sendNotificationToAll(contains("Gimnasio Central"), eq("SCHEDULE_UPDATED"), eq(100));
    }

    @Test
    void shouldThrowWhenScheduleNotFoundOnUpdate() {
        when(scheduleRepository.findById(404L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                scheduleService.updateSchedule(404L, schedule)
        );

        assertEquals("Schedule not found", ex.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldDeleteScheduleSuccessfully() {
        when(scheduleRepository.findById(100L)).thenReturn(Optional.of(schedule));

        scheduleService.deleteSchedule(100L);

        verify(scheduleRepository, times(1)).delete(schedule);
        verify(notificationService, times(1))
                .sendNotificationToAll(contains("Gimnasio Central"), eq("SCHEDULE_DELETED"), eq(100));
    }

    @Test
    void shouldThrowWhenScheduleNotFoundOnDelete() {
        when(scheduleRepository.findById(500L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                scheduleService.deleteSchedule(500L)
        );

        assertEquals("Schedule not found", ex.getMessage());
        verify(scheduleRepository, never()).delete(any());
    }
}