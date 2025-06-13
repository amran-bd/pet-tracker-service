package com.tractive.pet.tracker.unit;

import com.tractive.pet.tracker.entity.CatEntity;
import com.tractive.pet.tracker.entity.DogEntity;
import com.tractive.pet.tracker.entity.PetTrackerEntity;
import com.tractive.pet.tracker.enums.PetType;
import com.tractive.pet.tracker.enums.TrackerType;
import com.tractive.pet.tracker.exception.EntityNotFoundException;
import com.tractive.pet.tracker.exception.ValidationException;
import com.tractive.pet.tracker.mapper.PetTrackerMapper;
import com.tractive.pet.tracker.model.CatDto;
import com.tractive.pet.tracker.model.DogDto;
import com.tractive.pet.tracker.model.PetTrackerCustomDto;
import com.tractive.pet.tracker.model.PetTrackerDto;
import com.tractive.pet.tracker.model.response.PetsOutsideZoneResponse;
import com.tractive.pet.tracker.repository.PetTrackerRepository;
import com.tractive.pet.tracker.service.CatTrackerService;
import com.tractive.pet.tracker.service.DogTrackerService;
import com.tractive.pet.tracker.service.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@ExtendWith(MockitoExtension.class)
public class petTrackerServiceTest {

    @Mock
    private PetTrackerRepository petTrackerRepository;

    @Mock
    private PetTrackerMapper petTrackerMapper;

    @Mock
    private CatTrackerService catTrackerService;

    @Mock
    private DogTrackerService dogTrackerService;

    @InjectMocks
    private PetServiceImpl petService;

    protected CatEntity catEntity;
    protected DogEntity dogEntity;
    protected PetTrackerDto catDto;
    protected PetTrackerDto dogDto;
    protected List<PetTrackerCustomDto> cats;
    protected List<PetTrackerCustomDto> dogs;
    protected PetTrackerCustomDto petTrackerCustomDto;


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
        ((CatDto) catDto).setType(PetType.CAT);

        dogDto = new DogDto();
        dogDto.setId(2L);
        dogDto.setTrackerType(TrackerType.BIG);
        dogDto.setOwnerId(1);
        dogDto.setInZone(Boolean.TRUE);
        ((DogDto) dogDto).setType(PetType.DOG);

        petTrackerCustomDto = new PetTrackerCustomDto(TrackerType.SMALL,1L);
        cats = new ArrayList<>(List.of(petTrackerCustomDto));
        dogs = new ArrayList<>(List.of(petTrackerCustomDto));

    }

    ;

    @Test
    void addPetTracker_ShouldReturnSavedPet() {
        when(petTrackerMapper.mapToEntity(catDto)).thenReturn(catEntity);
        when(petTrackerRepository.save(catEntity)).thenReturn(catEntity);
        when(petTrackerMapper.mapToDto(catEntity)).thenReturn(catDto);

        PetTrackerDto result = petService.addPetTracker(catDto);

        assertNotNull(result);
        assertEquals(catDto, result);
        verify(petTrackerMapper).mapToEntity(catDto);
        verify(petTrackerRepository).save(catEntity);
        verify(petTrackerMapper).mapToDto(catEntity);
    }

    @Test
    void addPetTracker_ShouldThrowValidationException_WhenDtoIsNull() {
        assertThrows(ValidationException.class, () -> petService.addPetTracker(null));
    }

    @Test
    void addPetTracker_ShouldThrowValidationException_WhenNullInput() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> petService.addPetTracker(null));

        assertEquals("Pet Tracker should not null", exception.getMessage());

        verifyNoInteractions(petTrackerMapper);
        verifyNoInteractions(petTrackerRepository);
    }

    @Test
    void getPetByPetTrackerId_ShouldReturnPet_WhenExists() {
        when(petTrackerRepository.findById(1L)).thenReturn(Optional.of(catEntity));
        when(petTrackerMapper.mapToDto(catEntity)).thenReturn(catDto);

        PetTrackerDto result = petService.getPetByPetTrackerId(1L);

        assertEquals(catDto, result);
        verify(petTrackerRepository).findById(1L);
    }

    @Test
    void getPetListByPetType_ShouldReturnCatList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PetTrackerEntity> catPage = new PageImpl<>(List.of(catEntity), pageable, 1);

        when(petTrackerRepository.findAllByPetType(eq(CatEntity.class), any(Pageable.class)))
                .thenReturn(catPage);
        when(petTrackerMapper.mapToDto(catEntity)).thenReturn(catDto);

        Page<PetTrackerDto> result = petService.getPetListByPetType(PetType.CAT, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(catDto, result.getContent().get(0));
        verify(petTrackerRepository).findAllByPetType(CatEntity.class, pageable);
    }

    @Test
    void getPetListByPetType_ShouldReturnDogList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PetTrackerEntity> dogPage = new PageImpl<>(List.of(dogEntity), pageable, 1);

        when(petTrackerRepository.findAllByPetType(eq(DogEntity.class), any(Pageable.class)))
                .thenReturn(dogPage);
        when(petTrackerMapper.mapToDto(dogEntity)).thenReturn(dogDto);

        Page<PetTrackerDto> result = petService.getPetListByPetType(PetType.DOG, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(dogDto, result.getContent().get(0));
        verify(petTrackerRepository).findAllByPetType(DogEntity.class, pageable);
    }

    @Test
    void getPetListByPetType_ShouldThrowException_WhenNoPetsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(petTrackerRepository.findAllByPetType(any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        assertThrows(EntityNotFoundException.class, () -> petService.getPetListByPetType(PetType.CAT, pageable));
    }

    @Test
    void getPetByPageable_ShouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PetTrackerEntity> entityPage = new PageImpl<>(List.of(catEntity, dogEntity), pageable, 2);

        when(petTrackerRepository.findAll(pageable)).thenReturn(entityPage);
        when(petTrackerMapper.mapToDto(catEntity)).thenReturn(catDto);
        when(petTrackerMapper.mapToDto(dogEntity)).thenReturn(dogDto);

        Page<PetTrackerDto> result = petService.getPetByPageable(pageable);

        assertEquals(2, result.getContent().size());
        verify(petTrackerRepository).findAll(pageable);
    }

    @Test
    void getPetByPageable_ShouldThrowException_WhenNoPetsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(petTrackerRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(EntityNotFoundException.class,
                () -> petService.getPetByPageable(pageable));
    }

    @Test
    void getPetByPetTrackerId_ShouldThrowException_WhenNotFound() {
        when(petTrackerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> petService.getPetByPetTrackerId(99L));
    }

    @Test
    void getPetsOutsideZone_ShouldReturnCounts() {
        when(petTrackerRepository.countCatNotInZone()).thenReturn(cats);
        when(petTrackerRepository.countDogNotInZone()).thenReturn(dogs);

        PetsOutsideZoneResponse result = petService.getPetsOutsideZone();

        assertEquals(1, result.cats().size());
        assertEquals(1, result.dogs().size());
        verify(petTrackerRepository).countCatNotInZone();
        verify(petTrackerRepository).countDogNotInZone();
    }
}
