package com.tractive.pet.tracker.service;

import com.tractive.pet.tracker.entity.CatEntity;
import com.tractive.pet.tracker.entity.DogEntity;
import com.tractive.pet.tracker.entity.PetTrackerEntity;
import com.tractive.pet.tracker.enums.PetType;
import com.tractive.pet.tracker.exception.EntityNotFoundException;
import com.tractive.pet.tracker.exception.ValidationException;
import com.tractive.pet.tracker.mapper.PetTrackerMapper;
import com.tractive.pet.tracker.model.PetTrackerDto;
import com.tractive.pet.tracker.model.response.PetsOutsideZoneResponse;
import com.tractive.pet.tracker.repository.PetTrackerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Slf4j
@AllArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final PetTrackerRepository petTrackerRepository;
    private final PetTrackerMapper petTrackerMapper;

    @Override
    public PetTrackerDto addPetTracker(PetTrackerDto petTrackerDto) {
        if (petTrackerDto == null) {
            throw new ValidationException("Pet Tracker should not null");
        }
        var afterMap = petTrackerMapper.mapToEntity(petTrackerDto);
        var petTrackerEntity = petTrackerRepository.save(afterMap);
        return petTrackerMapper.mapToDto(petTrackerEntity);
    }

    @Override
    public Page<PetTrackerDto> getPetListByPetType(PetType petType, Pageable pageable) {
        log.info("PetServiceImpl::getPetListByPetType method called");
        Page<PetTrackerEntity> pages = switch (petType) {
            case CAT -> petTrackerRepository.findAllByPetType(CatEntity.class, pageable);
            case DOG -> petTrackerRepository.findAllByPetType(DogEntity.class, pageable);
            default -> Page.empty();
        };
        if (pages.isEmpty()) {
            throw new EntityNotFoundException("Pet Tracker list is empty");
        }
        return pages.map(petTrackerMapper::mapToDto);
    }

    @Override
    public Page<PetTrackerDto> getPetByPageable(Pageable pageable) {
        log.info("PetServiceImpl::getPetByPageable method called");
        var pages = petTrackerRepository.findAll(pageable);
        if (pages.isEmpty()) {
            throw new EntityNotFoundException("Pet Tracker list is empty");
        }
        return pages.map(petTrackerMapper::mapToDto);
    }

    @Override
    public PetTrackerDto getPetByPetTrackerId(Long petTrackerId) {
        log.info("PetServiceImpl::getPetByPetTrackerId method called");
        var pet = petTrackerRepository.findById(petTrackerId).orElseThrow(() -> new EntityNotFoundException("Pet Tracker not found"));
        return petTrackerMapper.mapToDto(pet);
    }

    @Override
    public PetsOutsideZoneResponse getPetsOutsideZone() {
        log.info("PetServiceImpl::getPetsOutsideZone method called");
        var countCatNotInZone = petTrackerRepository.countCatNotInZone();
        var countDogNotInZone = petTrackerRepository.countDogNotInZone();
        return new PetsOutsideZoneResponse(countCatNotInZone, countDogNotInZone);
    }
}
