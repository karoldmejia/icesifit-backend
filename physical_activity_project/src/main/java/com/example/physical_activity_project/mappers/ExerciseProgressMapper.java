package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.RoutineExercise;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExerciseProgressMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "routineExercise.id", target = "routineExerciseId")
    ExerciseProgressDTO entityToDto(ExerciseProgress progress);

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "routineExercise", expression = "java(mapRoutineExercise(dto.getRoutineExerciseId()))")
    @Mapping(target = "recommendations", ignore = true) // no se incluyen para evitar bucles
    ExerciseProgress dtoToEntity(ExerciseProgressDTO dto);

    default User mapUser(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    default RoutineExercise mapRoutineExercise(Long id) {
        if (id == null) return null;
        RoutineExercise re = new RoutineExercise();
        re.setId(id);
        return re;
    }
}
