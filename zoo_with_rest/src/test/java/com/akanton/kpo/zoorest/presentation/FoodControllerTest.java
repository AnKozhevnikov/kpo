package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.Food;
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

@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntityService entityService;

    @MockBean
    private IStatisticsService statisticsService;

    @Test
    void addFood_ShouldAddFoodAndReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/food/add")
                .param("name", "Meat")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(entityService).addFoodType(any(Food.class));
    }
    
    @Test
    void getFoodType_ShouldReturnAllFood() throws Exception {
        // Arrange
        Food food1 = new Food("Meat", 100);
        food1.setId(1);
        Food food2 = new Food("Hay", 200);
        food2.setId(2);
        
        List<Food> foods = Arrays.asList(food1, food2);
        
        when(statisticsService.getAllFood()).thenReturn(foods);
        
        // Act & Assert
        mockMvc.perform(get("/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Meat")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Hay")));
    }
    
    @Test
    void getFoodById_WithExistingId_ShouldReturnFood() throws Exception {
        // Arrange
        int foodId = 1;
        Food food = new Food("Meat", 100);
        food.setId(foodId);
        
        when(statisticsService.getFoodById(foodId)).thenReturn(food);
        
        // Act & Assert
        mockMvc.perform(get("/food/{id}", foodId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(foodId)))
                .andExpect(jsonPath("$.name", is("Meat")))
                .andExpect(jsonPath("$.amount", is(100)));
    }
    
    @Test
    void getFoodById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        int foodId = 999;
        when(statisticsService.getFoodById(foodId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/food/{id}", foodId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void removeFoodType_ShouldRemoveFoodAndReturnOk() throws Exception {
        // Arrange
        int foodId = 1;
        
        // Act & Assert
        mockMvc.perform(delete("/food/{id}", foodId))
                .andExpect(status().isOk());
        
        verify(entityService).removeFoodType(foodId);
    }
    
    @Test
    void whenExceptionThrown_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(statisticsService.getAllFood()).thenThrow(new RuntimeException("Error"));
        
        // Act & Assert
        mockMvc.perform(get("/food"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    void addFood_WithNegativeAmount_ShouldReturnBadRequest() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Amount cannot be negative"))
            .when(entityService).addFoodType(any(Food.class));
        
        // Act & Assert
        mockMvc.perform(post("/food/add")
                .param("name", "Meat")
                .param("amount", "-100")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError());
    }
} 