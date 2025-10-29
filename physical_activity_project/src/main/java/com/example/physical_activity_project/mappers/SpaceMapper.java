package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.SpaceDTO;
import com.example.physical_activity_project.model.Space;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SpaceMapper {

    SpaceMapper INSTANCE = Mappers.getMapper(SpaceMapper.class);

    SpaceDTO entityToDto(Space space);

    Space dtoToEntity(SpaceDTO dto);
}
