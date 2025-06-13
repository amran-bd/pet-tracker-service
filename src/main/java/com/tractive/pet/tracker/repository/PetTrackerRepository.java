package com.tractive.pet.tracker.repository;

import com.tractive.pet.tracker.entity.PetTrackerEntity;
import com.tractive.pet.tracker.model.PetTrackerCustomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
public interface PetTrackerRepository extends JpaRepository<PetTrackerEntity, Long> {

    @Query("""
            SELECT new com.tractive.pet.tracker.model.PetTrackerCustomDto(pet.trackerType, count(pet)) FROM PetTrackerEntity pet
                   WHERE TYPE(pet) = CatEntity AND pet.inZone = false AND pet.lostTracker = false 
                   GROUP BY pet.trackerType
            """)
    List<PetTrackerCustomDto> countCatNotInZone();

    @Query("""
            select new com.tractive.pet.tracker.model.PetTrackerCustomDto(pet.trackerType, count(pet)) from PetTrackerEntity pet
                        WHERE TYPE(pet) = DogEntity AND pet.inZone = false
                   group by pet.trackerType
            """)
    List<PetTrackerCustomDto> countDogNotInZone();

    @Query("""
            select pet from PetTrackerEntity pet where TYPE(pet)=:entityType
            """)
    Page<PetTrackerEntity> findAllByPetType(@Param("entityType") Class<? extends PetTrackerEntity> entityType, Pageable pageable);
}
