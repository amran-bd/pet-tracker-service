package com.tractive.pet.tracker.service;

import com.tractive.pet.tracker.enums.PetType;
import com.tractive.pet.tracker.model.PetTrackerDto;
import com.tractive.pet.tracker.model.response.PetsOutsideZoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
public interface PetService {
    PetsOutsideZoneResponse getPetsOutsideZone();

    PetTrackerDto addPetTracker(PetTrackerDto petTrackerDto);

    Page<PetTrackerDto> getPetListByPetType(PetType petType, Pageable pageable);

    Page<PetTrackerDto> getPetByPageable(Pageable pageable);

    PetTrackerDto getPetByPetTrackerId(final Long petTrackerId);
}
