package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.EventScheduleDTO;
import com.example.physical_activity_project.model.EventSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventScheduleMapper {

    EventScheduleMapper INSTANCE = Mappers.getMapper(EventScheduleMapper.class);

    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "schedule.startTime", target = "scheduleStartTime")
    @Mapping(source = "schedule.endTime", target = "scheduleEndTime")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    EventScheduleDTO entityToDto(EventSchedule eventSchedule);

    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "eventId", target = "event.id")
    EventSchedule dtoToEntity(EventScheduleDTO dto);
}
