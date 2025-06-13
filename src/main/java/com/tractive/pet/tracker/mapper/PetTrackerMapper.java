package com.tractive.pet.tracker.mapper;

import com.tractive.pet.tracker.entity.CatEntity;
import com.tractive.pet.tracker.entity.DogEntity;
import com.tractive.pet.tracker.entity.PetTrackerEntity;
import com.tractive.pet.tracker.model.CatDto;
import com.tractive.pet.tracker.model.DogDto;
import com.tractive.pet.tracker.model.PetTrackerDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetTrackerMapper {

    default PetTrackerEntity mapToEntity(PetTrackerDto tracker) {
        if (tracker == null) return null;
        if (tracker instanceof CatDto) return map((CatDto) tracker);
        if (tracker instanceof DogDto) return map((DogDto) tracker);
        throw new IllegalArgumentException("Unknown tracker type");
    }

    default PetTrackerDto mapToDto(PetTrackerEntity pet) {
        if (pet == null) return null;
        if (pet instanceof CatEntity) return map((CatEntity) pet);
        if (pet instanceof DogEntity) return map((DogEntity) pet);
        throw new IllegalArgumentException("Unknown pet type");
    }

    CatEntity map(CatDto dto);
    DogEntity map(DogDto dto);

    CatDto map(CatEntity entity);
    DogDto map(DogEntity entity);

    default void mapToEntity(PetTrackerDto tracker, PetTrackerEntity pet) {
        if (tracker instanceof CatDto && pet instanceof CatEntity) {
            update((CatDto) tracker, (CatEntity) pet);
        } else if (tracker instanceof DogDto && pet instanceof DogEntity) {
            update((DogDto) tracker, (DogEntity) pet);
        } else {
            throw new IllegalArgumentException("Type mismatch");
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(CatDto dto, @MappingTarget CatEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(DogDto dto, @MappingTarget DogEntity entity);
}