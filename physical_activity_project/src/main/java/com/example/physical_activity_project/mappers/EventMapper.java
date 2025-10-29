package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.EventDTO;
import com.example.physical_activity_project.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDTO entityToDto(Event event);

    Event dtoToEntity(EventDTO dto);
}
