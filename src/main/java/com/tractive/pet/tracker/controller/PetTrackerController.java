package com.tractive.pet.tracker.controller;

import com.tractive.pet.tracker.constant.ApiBaseURL;
import com.tractive.pet.tracker.enums.PetType;
import com.tractive.pet.tracker.model.PetTrackerDto;
import com.tractive.pet.tracker.model.response.ComApiResponse;
import com.tractive.pet.tracker.model.response.PetsOutsideZoneResponse;
import com.tractive.pet.tracker.service.PetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@RestController
@RequestMapping(path = ApiBaseURL.PET_TRACKER_BASE_URL)
@RequiredArgsConstructor
public class PetTrackerController {

    private final PetService petService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComApiResponse<PetTrackerDto>> addPetsTrackers(@RequestBody @Valid PetTrackerDto request) {
        return new ResponseEntity<>(ComApiResponse.<PetTrackerDto>builder()
                .data(petService.addPetTracker(request)).build(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/petType/{petType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPetListByPetType(@PathVariable @NotNull PetType petType, @PageableDefault(page = 0, size = 10, sort = "trackerType") Pageable pageable) {
        return new ResponseEntity<>(ComApiResponse.<Page<PetTrackerDto>>builder()
                .data(petService.getPetListByPetType(petType, pageable)).build(), HttpStatus.OK);
    }

    @GetMapping(path = "/statics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPetsOutsideZone() {
        return new ResponseEntity<>(ComApiResponse.<PetsOutsideZoneResponse>builder()
                .data(petService.getPetsOutsideZone()).build(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPetTrackerByPagination(@PageableDefault(page = 0, size = 10, sort = "trackerType") Pageable pageable) {
        return new ResponseEntity<>(ComApiResponse.<Page<PetTrackerDto>>builder()
                .data(petService.getPetByPageable(pageable)).build(), HttpStatus.OK);
    }

    @GetMapping(path = "{petTrackerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPetTrackerByPagination(@PathVariable @NotNull Long petTrackerId) {
        return new ResponseEntity<>(ComApiResponse.<PetTrackerDto>builder()
                .data(petService.getPetByPetTrackerId(petTrackerId)).build(), HttpStatus.OK);
    }
}
