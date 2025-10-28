package com.example.physical_activity_project.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.model.RoutineExercise;

@Mapper(componentModel = "spring")
public interface RoutineExerciseMapper {

    @Mapping(source = "exercise.id", target = "exerciseId")
    @Mapping(source = "routine.id", target = "routineId")
    RoutineExerciseDTO entityToDto(RoutineExercise entity);

    @Mapping(source = "exerciseId", target = "exercise.id")
    @Mapping(source = "routineId", target = "routine.id")
    RoutineExercise dtoToEntity(RoutineExerciseDTO dto);
}
