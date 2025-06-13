package com.tractive.pet.tracker.model.response;

import com.tractive.pet.tracker.model.PetTrackerCustomDto;

import java.util.List;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
public record PetsOutsideZoneResponse(List<PetTrackerCustomDto> cats, List<PetTrackerCustomDto> dogs) {
}
