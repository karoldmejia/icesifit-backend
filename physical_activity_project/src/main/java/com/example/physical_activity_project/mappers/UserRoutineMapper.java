package com.example.physical_activity_project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.dto.UserRoutineDTO;

@Mapper(componentModel = "spring")
public interface UserRoutineMapper {

    @Mapping(source = "routine.id", target = "routineId")
    @Mapping(source = "user.id", target = "userId")
    UserRoutineDTO entityToDto(UserRoutine entity);

    @Mapping(source = "routineId", target = "routine.id")
    @Mapping(source = "userId", target = "user.id")
    UserRoutine dtoToEntity(UserRoutineDTO dto);
}
