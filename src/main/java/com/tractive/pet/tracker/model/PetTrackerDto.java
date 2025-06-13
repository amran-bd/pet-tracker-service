package com.tractive.pet.tracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tractive.pet.tracker.enums.TrackerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY, property = "petType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CatDto.class, name = "CAT"),
        @JsonSubTypes.Type(value = DogDto.class, name = "DOG")
})
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract sealed class PetTrackerDto permits CatDto, DogDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "Tracker Type can't be null or empty")
    private TrackerType trackerType;
    @NotNull(message = "Owner Id can't be null or empty")
    @Positive(message = "Owner ID must be positive")
    private Integer ownerId;
    @NotNull(message = "In zone can't be null or empty")
    private Boolean inZone;
}
