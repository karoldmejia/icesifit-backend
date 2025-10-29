package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.RecommendationDTO;
import com.example.physical_activity_project.model.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    RecommendationMapper INSTANCE = Mappers.getMapper(RecommendationMapper.class);

    @Mapping(source = "trainer.id", target = "trainerId")
    @Mapping(source = "trainer.name", target = "trainerName")
    @Mapping(source = "exerciseProgress.id", target = "progressId")
    @Mapping(source = "exerciseProgress.user.id", target = "userId")
    RecommendationDTO entityToDto(Recommendation recommendation);

    @Mapping(source = "trainerId", target = "trainer.id")
    @Mapping(source = "progressId", target = "exerciseProgress.id")
    Recommendation dtoToEntity(RecommendationDTO dto);
}