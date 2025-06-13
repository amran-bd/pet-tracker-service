package com.tractive.pet.tracker.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tractive.pet.tracker.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("CAT")
public non-sealed class CatDto extends PetTrackerDto {
    private PetType type;
    private Boolean lostTracker;
}