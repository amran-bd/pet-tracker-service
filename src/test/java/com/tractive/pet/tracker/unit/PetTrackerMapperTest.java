package com.tractive.pet.tracker.unit;

import com.tractive.pet.tracker.entity.CatEntity;
import com.tractive.pet.tracker.entity.DogEntity;
import com.tractive.pet.tracker.entity.PetTrackerEntity;
import com.tractive.pet.tracker.enums.TrackerType;
import com.tractive.pet.tracker.mapper.PetTrackerMapper;
import com.tractive.pet.tracker.mapper.PetTrackerMapperImpl;
import com.tractive.pet.tracker.model.CatDto;
import com.tractive.pet.tracker.model.DogDto;
import com.tractive.pet.tracker.model.PetTrackerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Fail.fail;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@ExtendWith(MockitoExtension.class)
public class PetTrackerMapperTest {

    @Spy
    private PetTrackerMapper mapper;

    protected CatEntity catEntity;
    protected DogEntity dogEntity;
    protected CatDto catDto;
    protected DogDto dogDto;

    @BeforeEach
    void setUp() {
        catEntity = new CatEntity();
        catEntity.setId(1L);
        catEntity.setTrackerType(TrackerType.SMALL);
        catEntity.setOwnerId(1);
        catEntity.setInZone(Boolean.TRUE);

        dogEntity = new DogEntity();
        dogEntity.setId(2L);
        dogEntity.setTrackerType(TrackerType.BIG);
        dogEntity.setOwnerId(1);
        dogEntity.setInZone(Boolean.TRUE);

        catDto = new CatDto();
        catDto.setId(1L);
        catDto.setTrackerType(TrackerType.SMALL);
        catDto.setOwnerId(1);
        catDto.setInZone(Boolean.TRUE);

        dogDto = new DogDto();
        dogDto.setId(2L);
        dogDto.setTrackerType(TrackerType.BIG);
        dogDto.setOwnerId(1);
        dogDto.setInZone(Boolean.TRUE);

    }

    @Test
    void mapToEntity_ShouldReturnCatEntity_WhenCatDtoProvided() {
        when(mapper.map(catDto)).thenReturn(catEntity);

        PetTrackerEntity result = mapper.mapToEntity(catDto);

        assertInstanceOf(CatEntity.class, result);
        verify(mapper).map(catDto);
    }

    @Test
    void mapToEntity_ShouldReturnDogEntity_WhenDogDtoProvided() {
        when(mapper.map(dogDto)).thenReturn(dogEntity);

        PetTrackerEntity result = mapper.mapToEntity(dogDto);

        assertInstanceOf(DogEntity.class, result);
        verify(mapper).map(dogDto);
    }

    @Test
    void mapToEntity_ShouldReturnNull_WhenNullInput() {
        assertNull(mapper.mapToEntity(null));
    }

    //Reflection Way Test
    @Test
    void mapper_ShouldHandleAllPermittedSubtypes() {
        Class<?>[] permittedSubtypes = PetTrackerDto.class.getPermittedSubclasses();

        for (Class<?> subtype : permittedSubtypes) {
            try {
                PetTrackerDto dto = (PetTrackerDto) subtype.getConstructor().newInstance();
                assertDoesNotThrow(() -> mapper.mapToEntity(dto));
            } catch (Exception e) {
                fail("Failed to test with permitted subtype " + subtype.getSimpleName());
            }
        }
    }


    @Test
    void mapToDto_ShouldReturnCatDto_WhenCatEntityProvided() {
        when(mapper.map(catEntity)).thenReturn(catDto);

        PetTrackerDto result = mapper.mapToDto(catEntity);

        assertInstanceOf(CatDto.class, result);
        verify(mapper).map(catEntity);
    }

    @Test
    void mapToDto_ShouldReturnDogDto_WhenDogEntityProvided() {
        when(mapper.map(dogEntity)).thenReturn(dogDto);

        PetTrackerDto result = mapper.mapToDto(dogEntity);

        assertInstanceOf(DogDto.class, result);
        verify(mapper).map(dogEntity);
    }

    @Test
    void mapToDto_ShouldReturnNull_WhenNullInput() {
        assertNull(mapper.mapToDto(null));
    }

    @Test
    void mapToDto_ShouldThrowException_WhenUnknownType() {
        PetTrackerEntity unknownEntity = new PetTrackerEntity() {}; // Anonymous subclass
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToDto(unknownEntity));
    }

    @Test
    void map_ShouldConvertCatDtoToCatEntity() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();
        CatEntity result = realMapper.map(catDto);

        assertNotNull(result);
        assertEquals(catDto.getId(), result.getId());
        assertEquals(catDto.getTrackerType(), result.getTrackerType());
    }

    @Test
    void map_ShouldConvertDogDtoToDogEntity() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();
        DogEntity result = realMapper.map(dogDto);

        assertNotNull(result);
        assertEquals(dogDto.getId(), result.getId());
        assertEquals(dogDto.getTrackerType(), result.getTrackerType());
    }

    @Test
    void map_ShouldConvertCatEntityToCatDto() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();
        CatDto result = realMapper.map(catEntity);

        assertNotNull(result);
        assertEquals(catEntity.getId(), result.getId());
        assertEquals(catEntity.getTrackerType(), result.getTrackerType());
    }

    @Test
    void mapToEntityWithTarget_ShouldUpdateCatEntity_WhenCatDtoProvided() {
        CatEntity target = new CatEntity();
        doNothing().when(mapper).update(catDto, target);

        mapper.mapToEntity(catDto, target);

        verify(mapper).update(catDto, target);
    }

    @Test
    void mapToEntityWithTarget_ShouldThrowException_WhenTypeMismatch() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(catDto, dogEntity));
    }

    @Test
    void update_ShouldModifyCatEntity_WhenCatDtoProvided() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();
        CatEntity target = new CatEntity();
        realMapper.update(catDto, target);

        assertEquals(catDto.getId(), target.getId());
        assertEquals(catDto.getTrackerType(), target.getTrackerType());
    }

    @Test
    void mapToEntityWithTarget_ShouldThrowException_WhenTypeMismatchForDog() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(dogDto, catEntity));
    }

    @Test
    void update_ShouldModifyCatEntity_WhenCatDtoProvidedForDog() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();
        DogEntity target = new DogEntity();
        realMapper.update(dogDto, target);

        assertEquals(dogDto.getId(), target.getId());
        assertEquals(dogDto.getTrackerType(), target.getTrackerType());
    }

    @Test
    void update_ShouldIgnoreNullValues() {
        PetTrackerMapper realMapper = new PetTrackerMapperImpl();

        CatDto partialDto = new CatDto();
        catDto.setId(null);
        catDto.setTrackerType(null);
        catDto.setOwnerId(null);
        catDto.setInZone(Boolean.FALSE);

        CatEntity target = new CatEntity();
        catEntity.setId(1L);
        catEntity.setTrackerType(TrackerType.SMALL);
        catEntity.setOwnerId(1);
        catEntity.setInZone(Boolean.TRUE);

        realMapper.update(partialDto, target);

        assertNull(target.getId());
        assertNull(target.getTrackerType());
    }

    @Test
    void mapToEntityWithTarget_ShouldHandleNullInput() {
        CatEntity target = new CatEntity();
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(null, target));
    }

    @Test
    void mapToEntityWithTarget_ShouldHandleNullTarget() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(catDto, null));
    }

    @Test
    void mapToEntityWithTarget_ShouldHandleNullInputForDog() {
        DogEntity target = new DogEntity();
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(null, target));
    }

    @Test
    void mapToEntityWithTarget_ShouldHandleNullTargetForDog() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapToEntity(dogDto, null));
    }
}
