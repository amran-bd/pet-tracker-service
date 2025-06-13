package com.tractive.pet.tracker.intregration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tractive.pet.tracker.enums.PetType;
import com.tractive.pet.tracker.enums.TrackerType;
import com.tractive.pet.tracker.exception.EntityNotFoundException;
import com.tractive.pet.tracker.exception.ValidationException;
import com.tractive.pet.tracker.mapper.PetTrackerMapper;
import com.tractive.pet.tracker.model.CatDto;
import com.tractive.pet.tracker.model.DogDto;
import com.tractive.pet.tracker.model.PetTrackerCustomDto;
import com.tractive.pet.tracker.model.PetTrackerDto;
import com.tractive.pet.tracker.model.response.PetsOutsideZoneResponse;
import com.tractive.pet.tracker.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.tractive.pet.tracker.enums.PetType.CAT;
import static com.tractive.pet.tracker.enums.PetType.DOG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true"
})
public class PetTrackerControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PetTrackerMapper petTrackerMapper;

    @MockBean
    protected PetService petService;

    protected PetTrackerDto catDto;
    protected PetTrackerDto dogDto;
    protected Page<PetTrackerDto> petPage;
    protected PetsOutsideZoneResponse staticsResponse;
    protected List<PetTrackerCustomDto> cats;
    protected List<PetTrackerCustomDto> dogs;
    protected PetTrackerCustomDto petTrackerCustomDto;

    @BeforeEach
    void setUp() {
        catDto = new CatDto();
        catDto.setId(1L);
        catDto.setTrackerType(TrackerType.SMALL);
        catDto.setOwnerId(1);
        catDto.setInZone(Boolean.TRUE);
        ((CatDto) catDto).setType(PetType.CAT);

        dogDto = new DogDto();
        dogDto.setId(2L);
        dogDto.setTrackerType(TrackerType.BIG);
        dogDto.setOwnerId(1);
        dogDto.setInZone(Boolean.TRUE);
        ((DogDto) dogDto).setType(DOG);

        petPage = new PageImpl<>(List.of(catDto, dogDto), PageRequest.of(0, 10), 2);

        petTrackerCustomDto = new PetTrackerCustomDto(TrackerType.SMALL,1L);
        cats = new ArrayList<>(List.of(petTrackerCustomDto));
        dogs = new ArrayList<>(List.of(petTrackerCustomDto));

        staticsResponse = new PetsOutsideZoneResponse(cats, dogs);
    }

    @Test
    void createPetTracker_ShouldReturnCreated() throws Exception {
        when(petService.addPetTracker(any(PetTrackerDto.class))).thenReturn(catDto);

        mockMvc.perform(post("/api/v1/pets/trackers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.id").value(catDto.getId()))
                .andExpect(jsonPath("$.data.type").value(CAT.name()))
                .andDo(print());
    }

    @Test
    void createPetTracker_ShouldReturnCreatedForDog() throws Exception {
        when(petService.addPetTracker(any(PetTrackerDto.class))).thenReturn(dogDto);

        mockMvc.perform(post("/api/v1/pets/trackers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dogDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.id").value(dogDto.getId()))
                .andExpect(jsonPath("$.data.type").value(DOG.name()))
                .andDo(print());
    }

    @Test
    void createPetTracker_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String json = """
                 { "ownerId": 1, "petType": "CAT2", "trackerType": "SMALL", "inZone": false, "lostTracker": false }
                """;
        mockMvc.perform(post("/api/v1/pets/trackers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(json)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void getPetsByType_ShouldReturnPaginatedResults() throws Exception {
        when(petService.getPetListByPetType(eq(CAT), any(PageRequest.class))).thenReturn(petPage);

        mockMvc.perform(get("/api/v1/pets/trackers/petType/CAT")
                        .param("page", "0")
                        .param("size", "10")
                        .param("petTrackerTpe", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].type").value(CAT.name()))
                .andDo(print());
    }

    @Test
    void getPetsByType_ShouldReturnPaginatedResultsForDog() throws Exception {
        when(petService.getPetListByPetType(eq(DOG), any(PageRequest.class))).thenReturn(petPage);

        mockMvc.perform(get("/api/v1/pets/trackers/petType/DOG")
                        .param("page", "0")
                        .param("size", "10")
                        .param("petTrackerTpe", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[1].type").value(DOG.name()))
                .andDo(print());
    }

    @Test
    void getPetsByType_WithInvalidType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/pets/trackers/petType/INVALID_TYPE")).andExpect(status().isBadRequest());
    }

    @Test
    void getPetsOutsideZone_ShouldReturnStatistics() throws Exception {
        when(petService.getPetsOutsideZone()).thenReturn(staticsResponse);

        mockMvc.perform(get("/api/v1/pets/trackers/statics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cats.length()").value(1))
                .andExpect(jsonPath("$.data.dogs.length()").value(1))
                .andDo(print());
    }

    @Test
    void getAllPetTrackers_ShouldReturnPaginatedResults() throws Exception {
        when(petService.getPetByPageable(any(PageRequest.class)))
                .thenReturn(petPage);

        mockMvc.perform(get("/api/v1/pets/trackers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andDo(print());
    }

    @Test
    void getPetTrackerById_ShouldReturnPet() throws Exception {
        when(petService.getPetByPetTrackerId(1L)).thenReturn(catDto);

        mockMvc.perform(get("/api/v1/pets/trackers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.type").value(CAT.name()))
                .andDo(print());
    }

    @Test
    void getPetTrackerById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(petService.getPetByPetTrackerId(999L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/pets/trackers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {
        when(petService.getPetByPetTrackerId(anyLong()))
                .thenThrow(new RuntimeException("Unexpected database error"));

        mockMvc.perform(get("/api/v1/pets/trackers/123"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected database error"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addPetTracker_ShouldReturn400_WhenValidationFails() throws Exception {
        when(petService.addPetTracker(any())).thenThrow(new ValidationException("Invalid pet data"));
        String invalidPetJson = "{}";

        mockMvc.perform(post("/api/v1/pets/trackers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPetJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addPetTracker_ShouldReturn400_WhenRequestBodyIsInvalid() throws Exception {
        String malformedJson = "{ invalid json }";
        mockMvc.perform(post("/api/v1/pets/trackers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}
