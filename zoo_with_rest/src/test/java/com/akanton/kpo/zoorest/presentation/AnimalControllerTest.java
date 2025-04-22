package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalService;
import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalTransferService;
import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.SexType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntityService entityService;

    @MockBean
    private IStatisticsService statisticsService;

    @MockBean
    private IAnimalTransferService animalTransferService;

    @MockBean
    private IAnimalService animalService;

    @Test
    void getAllAnimals_ShouldReturnAllAnimals() throws Exception {
        // Arrange
        Animal lion = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        Animal tiger = new Animal("Tiger", SexType.FEMALE, OffsetDateTime.now(), 2, 2, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        List<Animal> animals = Arrays.asList(lion, tiger);
        
        when(statisticsService.getAllAnimals()).thenReturn(animals);
        
        // Act & Assert
        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Lion")))
                .andExpect(jsonPath("$[1].name", is("Tiger")));
    }
    
    @Test
    void getAnimalById_WithExistingId_ShouldReturnAnimal() throws Exception {
        // Arrange
        int animalId = 1;
        Animal lion = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        lion.setId(animalId);
        
        when(statisticsService.getAnimalById(animalId)).thenReturn(lion);
        
        // Act & Assert
        mockMvc.perform(get("/animals/{id}", animalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(animalId)))
                .andExpect(jsonPath("$.name", is("Lion")));
    }
    
    @Test
    void getAnimalById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        int animalId = 999;
        when(statisticsService.getAnimalById(animalId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/animals/{id}", animalId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void addAnimal_ShouldAddAnimalAndReturnOk() throws Exception {
        // Arrange
        String birthDateStr = OffsetDateTime.now().toString();
        
        // Act & Assert
        mockMvc.perform(post("/animals")
                .param("name", "Lion")
                .param("sex", SexType.MALE.toString())
                .param("birthDate", birthDateStr)
                .param("favouriteFoodId", "1")
                .param("enclosureId", "1")
                .param("healthy", "true")
                .param("animalFoodType", AnimalFoodType.PREDATOR.toString())
                .param("animalHabitatType", AnimalHabitatType.TERRASTRIAL.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(entityService).addAnimal(any(Animal.class));
    }
    
    @Test
    void transferAnimal_ShouldTransferAnimalAndReturnOk() throws Exception {
        // Arrange
        int animalId = 1;
        int newEnclosureId = 2;
        
        // Act & Assert
        mockMvc.perform(put("/animals/transfer")
                .param("animalId", String.valueOf(animalId))
                .param("newEnclosureId", String.valueOf(newEnclosureId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(animalTransferService).transferAnimal(animalId, newEnclosureId);
    }
    
    @Test
    void healAnimal_ShouldHealAnimalAndReturnOk() throws Exception {
        // Arrange
        int animalId = 1;
        
        // Act & Assert
        mockMvc.perform(put("/animals/heal")
                .param("animalId", String.valueOf(animalId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(animalService).healAnimal(animalId);
    }
    
    @Test
    void removeAnimal_ShouldRemoveAnimalAndReturnOk() throws Exception {
        // Arrange
        int animalId = 1;
        
        // Act & Assert
        mockMvc.perform(delete("/animals/{id}", animalId))
                .andExpect(status().isOk());
        
        verify(entityService).removeAnimal(animalId);
    }
    
    @Test
    void whenExceptionThrown_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(statisticsService.getAllAnimals()).thenThrow(new RuntimeException("Error"));
        
        // Act & Assert
        mockMvc.perform(get("/animals"))
                .andExpect(status().isInternalServerError());
    }
} 