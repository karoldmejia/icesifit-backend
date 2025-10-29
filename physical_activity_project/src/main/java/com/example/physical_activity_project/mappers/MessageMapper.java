package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.MessageDTO;
import com.example.physical_activity_project.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "trainer.id", target = "trainerId")
    @Mapping(source = "trainer.name", target = "trainerName")
    MessageDTO entityToDto(Message message);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "trainerId", target = "trainer.id")
    Message dtoToEntity(MessageDTO dto);
}
