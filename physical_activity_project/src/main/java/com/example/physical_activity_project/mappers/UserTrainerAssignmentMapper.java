package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.UserTrainerAssignmentDTO;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserTrainerAssignmentMapper {

    @Mapping(source = "trainer.id", target = "trainerId")
    @Mapping(source = "trainer.name", target = "trainerName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    UserTrainerAssignmentDTO entityToDto(UserTrainerAssignment assignment);

    @Mapping(target = "trainer", expression = "java(mapUser(dto.getTrainerId()))")
    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    UserTrainerAssignment dtoToEntity(UserTrainerAssignmentDTO dto);

    default User mapUser(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
