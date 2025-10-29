package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserEvent;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.repository.IUserEventRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.IUserEventService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserEventServiceImpl implements IUserEventService {

    private final IUserEventRepository userEventRepository;
    private final IUserRepository userRepository;
    private final IEventRepository eventRepository;
    private final INotificationService notificationService;

    public UserEventServiceImpl(IUserEventRepository userEventRepository,
                                IUserRepository userRepository,
                                IEventRepository eventRepository,
                                INotificationService notificationService) {
        this.userEventRepository = userEventRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    @Override
    public UserEvent registerUserToEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setAttended(false);
        userEvent.setRegistrationDate(new Timestamp(System.currentTimeMillis()));

        UserEvent saved = userEventRepository.save(userEvent);

        notificationService.sendNotification(
                userId,
                "Te has inscrito al evento: " + event.getName(),
                "USER_EVENT_CREATE",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public List<UserEvent> getUserEvents(Long userId) {
        return userEventRepository.findByUserId(userId);
    }

    @Override
    public UserEvent markAttendance(Long userEventId, Boolean attended) {
        UserEvent ue = userEventRepository.findById(userEventId)
                .orElseThrow(() -> new RuntimeException("UserEvent not found"));
        ue.setAttended(attended);
        return userEventRepository.save(ue);
    }

    @Override
    public void cancelRegistration(Long userEventId) {
        UserEvent ue = userEventRepository.findById(userEventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        userEventRepository.delete(ue);

        notificationService.sendNotification(
                ue.getUser().getId(),
                "Has cancelado tu inscripción al evento: " + ue.getEvent().getName(),
                "USER_EVENT_DELETE",
                Math.toIntExact(userEventId)
        );
    }

    @Override
    public List<UserEvent> getAllUserEvents() {
        return userEventRepository.findAll();
    }

    @Override
    public List<UserEvent> getUserEventsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return userEventRepository.findByEventId(event.getId());
    }

}

