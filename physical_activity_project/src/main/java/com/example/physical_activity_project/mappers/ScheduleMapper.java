package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ScheduleDTO;
import com.example.physical_activity_project.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "space.id", target = "spaceId")
    ScheduleDTO entityToDto(Schedule schedule);

    @Mapping(source = "spaceId", target = "space.id")
    Schedule dtoToEntity(ScheduleDTO dto);
}