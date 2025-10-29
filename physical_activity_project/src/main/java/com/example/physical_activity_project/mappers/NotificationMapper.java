package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.NotificationDTO;
import com.example.physical_activity_project.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    NotificationDTO entityToDto(Notification notification);

    @Mapping(source = "userId", target = "user.id")
    Notification dtoToEntity(NotificationDTO dto);
}
