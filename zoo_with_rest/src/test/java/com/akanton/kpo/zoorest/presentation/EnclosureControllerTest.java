package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEnclosureService;
import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnclosureController.class)
class EnclosureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntityService entityService;

    @MockBean
    private IStatisticsService statisticsService;

    @MockBean
    private IEnclosureService enclosureService;

    @Test
    void getAllEnclosures_ShouldReturnAllEnclosures() throws Exception {
        // Arrange
        Enclosure enclosure1 = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure1.setId(1);
        Enclosure enclosure2 = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 20);
        enclosure2.setId(2);
        
        List<Enclosure> enclosures = Arrays.asList(enclosure1, enclosure2);
        
        when(statisticsService.getAllEnclosures()).thenReturn(enclosures);
        
        // Act & Assert
        mockMvc.perform(get("/enclosures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].animalFoodType", is("PREDATOR")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].animalFoodType", is("HERBIVORE")));
    }
    
    @Test
    void getEnclosureById_WithExistingId_ShouldReturnEnclosure() throws Exception {
        // Arrange
        int enclosureId = 1;
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        when(statisticsService.getEnclosureById(enclosureId)).thenReturn(enclosure);
        
        // Act & Assert
        mockMvc.perform(get("/enclosures/{id}", enclosureId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(enclosureId)))
                .andExpect(jsonPath("$.animalFoodType", is("PREDATOR")));
    }
    
    @Test
    void getEnclosureById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        int enclosureId = 999;
        when(statisticsService.getEnclosureById(enclosureId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/enclosures/{id}", enclosureId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void addEnclosure_ShouldAddEnclosureAndReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/enclosures/add")
                .param("animalFoodType", AnimalFoodType.PREDATOR.toString())
                .param("animalHabitatType", AnimalHabitatType.TERRASTRIAL.toString())
                .param("capacity", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(entityService).addEnclosure(any(Enclosure.class));
    }
    
    @Test
    void cleanEnclosure_ShouldCleanEnclosureAndReturnOk() throws Exception {
        // Arrange
        int enclosureId = 1;
        
        // Act & Assert
        mockMvc.perform(put("/enclosures/clean")
                .param("enclosureId", String.valueOf(enclosureId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(enclosureService).cleanEnclosure(enclosureId);
    }
    
    @Test
    void removeEnclosure_ShouldRemoveEnclosureAndReturnOk() throws Exception {
        // Arrange
        int enclosureId = 1;
        
        // Act & Assert
        mockMvc.perform(delete("/enclosures/{id}", enclosureId))
                .andExpect(status().isOk());
        
        verify(entityService).removeEnclosure(enclosureId);
    }
    
    @Test
    void whenExceptionThrown_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(statisticsService.getAllEnclosures()).thenThrow(new RuntimeException("Error"));
        
        // Act & Assert
        mockMvc.perform(get("/enclosures"))
                .andExpect(status().isInternalServerError());
    }
} 