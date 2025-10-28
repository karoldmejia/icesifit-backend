package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Schedule;
import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.repository.IScheduleRepository;
import com.example.physical_activity_project.repository.ISpaceRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.IScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    private final IScheduleRepository scheduleRepository;
    private final ISpaceRepository spaceRepository;
    private final INotificationService notificationService;

    public ScheduleServiceImpl(IScheduleRepository scheduleRepository,
                               ISpaceRepository spaceRepository,
                               INotificationService notificationService) {
        this.scheduleRepository = scheduleRepository;
        this.spaceRepository = spaceRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Schedule createSchedule(Long spaceId, Schedule schedule) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));
        schedule.setSpace(space);
        Schedule saved = scheduleRepository.save(schedule);

        notificationService.sendNotificationToAll(
                "Se ha creado un nuevo horario en el espacio: " + space.getName(),
                "SCHEDULE_CREATED",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }


    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> getSchedulesBySpace(Long spaceId) {
        return scheduleRepository.findBySpaceId(spaceId);
    }

    @Override
    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        existing.setDayOfWeek(updatedSchedule.getDayOfWeek());
        existing.setStartTime(updatedSchedule.getStartTime());
        existing.setEndTime(updatedSchedule.getEndTime());
        Schedule saved = scheduleRepository.save(existing);

        notificationService.sendNotificationToAll(
                "Se ha actualizado un horario del espacio: " + existing.getSpace().getName(),
                "SCHEDULE_UPDATED",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public void deleteSchedule(Long id) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        scheduleRepository.delete(existing);

        notificationService.sendNotificationToAll(
                "Se ha eliminado un horario del espacio: " + existing.getSpace().getName(),
                "SCHEDULE_DELETED",
                Math.toIntExact(existing.getId())
        );
    }

}
