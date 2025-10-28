package com.example.physical_activity_project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.dto.RoutineDTO;

@Mapper(componentModel = "spring")
public interface RoutineMapper {
    RoutineDTO entityToDto(Routine routine);
    Routine dtoToEntity(RoutineDTO routineDTO);
}
