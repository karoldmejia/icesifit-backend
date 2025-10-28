package com.example.physical_activity_project.mappers;

import org.mapstruct.Mapper;
import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.model.Exercise;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseDTO entityToDto(Exercise exercise);
    Exercise dtoToEntity(ExerciseDTO exerciseDTO);
}
