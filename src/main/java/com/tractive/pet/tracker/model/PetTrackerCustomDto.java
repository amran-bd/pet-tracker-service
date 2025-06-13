package com.tractive.pet.tracker.model;

import com.tractive.pet.tracker.enums.TrackerType;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
public record PetTrackerCustomDto(TrackerType trackerType, Long count) {
}
