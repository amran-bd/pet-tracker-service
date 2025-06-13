package com.tractive.pet.tracker.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Entity
@Table(name = "dog")
@DiscriminatorValue("DOG")
public class DogEntity extends PetTrackerEntity implements Serializable {
}
