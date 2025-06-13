package com.tractive.pet.tracker.repository;

import com.tractive.pet.tracker.entity.DogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Repository
public interface DogTrackerRepository extends JpaRepository<DogEntity, Long> {
}
