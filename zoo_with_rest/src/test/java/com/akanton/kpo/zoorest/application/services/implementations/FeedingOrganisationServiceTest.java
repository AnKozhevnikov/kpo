package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import com.akanton.kpo.zoorest.domain.Food;
import com.akanton.kpo.zoorest.domain.SexType;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FeedingScheduleRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedingOrganisationServiceTest {

    @Mock
    private FeedingScheduleRepository feedingScheduleRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private FeedingOrganisationService feedingOrganisationService;

    @Test
    void feedAnimal_ShouldFeedAnimal_WhenAllConditionsAreMet() {
        // Arrange
        int feedingScheduleId = 1;
        int animalId = 1;
        int foodId = 1;
        int feedAmount = 5;
        
        FeedingSchedule feedingSchedule = new FeedingSchedule(animalId, foodId, LocalTime.of(8, 0), feedAmount);
        feedingSchedule.setId(feedingScheduleId);
        
        Food food = new Food("Meat", 100);
        food.setId(foodId);
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        animal.setId(animalId);
        
        when(feedingScheduleRepository.findById(feedingScheduleId)).thenReturn(Optional.of(feedingSchedule));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        
        // Act
        feedingOrganisationService.feedAnimal(feedingScheduleId);
        
        // Assert
        assertEquals(95, food.getAmount()); // 100 - 5 = 95
        assertNotNull(feedingSchedule.getLastFed());
        assertNotNull(animal.getLastFed());
        
        verify(foodRepository).save(food);
        verify(feedingScheduleRepository).save(feedingSchedule);
        verify(animalRepository).save(animal);
    }
    
    @Test
    void feedAnimal_ShouldThrowException_WhenFeedingScheduleNotFound() {
        // Arrange
        int feedingScheduleId = 999;
        
        when(feedingScheduleRepository.findById(feedingScheduleId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedingOrganisationService.feedAnimal(feedingScheduleId);
        });
        
        assertEquals("Feeding schedule not found", exception.getMessage());
        verify(feedingScheduleRepository).findById(feedingScheduleId);
        verifyNoMoreInteractions(feedingScheduleRepository, foodRepository, animalRepository);
    }
    
    @Test
    void feedAnimal_ShouldThrowException_WhenFoodNotFound() {
        // Arrange
        int feedingScheduleId = 1;
        int animalId = 1;
        int foodId = 999;
        
        FeedingSchedule feedingSchedule = new FeedingSchedule(animalId, foodId, LocalTime.of(8, 0), 5);
        feedingSchedule.setId(feedingScheduleId);
        
        when(feedingScheduleRepository.findById(feedingScheduleId)).thenReturn(Optional.of(feedingSchedule));
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedingOrganisationService.feedAnimal(feedingScheduleId);
        });
        
        assertEquals("Food not found", exception.getMessage());
        verify(feedingScheduleRepository).findById(feedingScheduleId);
        verify(foodRepository).findById(foodId);
        verifyNoMoreInteractions(feedingScheduleRepository, foodRepository, animalRepository);
    }
    
    @Test
    void feedAnimal_ShouldThrowException_WhenAnimalNotFound() {
        // Arrange
        int feedingScheduleId = 1;
        int animalId = 999;
        int foodId = 1;
        
        FeedingSchedule feedingSchedule = new FeedingSchedule(animalId, foodId, LocalTime.of(8, 0), 5);
        feedingSchedule.setId(feedingScheduleId);
        
        Food food = new Food("Meat", 100);
        food.setId(foodId);
        
        when(feedingScheduleRepository.findById(feedingScheduleId)).thenReturn(Optional.of(feedingSchedule));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedingOrganisationService.feedAnimal(feedingScheduleId);
        });
        
        assertEquals("Animal not found", exception.getMessage());
        verify(feedingScheduleRepository).findById(feedingScheduleId);
        verify(foodRepository).findById(foodId);
        verify(animalRepository).findById(animalId);
        verifyNoMoreInteractions(feedingScheduleRepository, foodRepository, animalRepository);
    }
    
    @Test
    void feedAnimal_ShouldThrowException_WhenNotEnoughFood() {
        // Arrange
        int feedingScheduleId = 1;
        int animalId = 1;
        int foodId = 1;
        int feedAmount = 50;
        
        FeedingSchedule feedingSchedule = new FeedingSchedule(animalId, foodId, LocalTime.of(8, 0), feedAmount);
        feedingSchedule.setId(feedingScheduleId);
        
        Food food = new Food("Meat", 20); // Less than required amount
        food.setId(foodId);
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        animal.setId(animalId);
        
        when(feedingScheduleRepository.findById(feedingScheduleId)).thenReturn(Optional.of(feedingSchedule));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedingOrganisationService.feedAnimal(feedingScheduleId);
        });
        
        assertEquals("Not enough food available", exception.getMessage());
        verify(feedingScheduleRepository).findById(feedingScheduleId);
        verify(foodRepository).findById(foodId);
        verify(animalRepository).findById(animalId);
        verifyNoMoreInteractions(feedingScheduleRepository, foodRepository, animalRepository);
    }
    
    @Test
    void addFeedingSchedule_ShouldSaveFeedingSchedule() {
        // Arrange
        FeedingSchedule feedingSchedule = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        
        // Act
        feedingOrganisationService.addFeedingSchedule(feedingSchedule);
        
        // Assert
        verify(feedingScheduleRepository).save(feedingSchedule);
    }
    
    @Test
    void addFoodAmount_ShouldIncreaseFoodAmount_WhenFoodExists() {
        // Arrange
        int foodId = 1;
        int initialAmount = 100;
        int additionalAmount = 50;
        
        Food food = new Food("Meat", initialAmount);
        food.setId(foodId);
        
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        
        // Act
        feedingOrganisationService.addFoodAmount(foodId, additionalAmount);
        
        // Assert
        assertEquals(initialAmount + additionalAmount, food.getAmount());
        verify(foodRepository).save(food);
    }
    
    @Test
    void addFoodAmount_ShouldThrowException_WhenFoodNotFound() {
        // Arrange
        int foodId = 999;
        int additionalAmount = 50;
        
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedingOrganisationService.addFoodAmount(foodId, additionalAmount);
        });
        
        assertEquals("Food not found", exception.getMessage());
        verify(foodRepository).findById(foodId);
        verifyNoMoreInteractions(foodRepository);
    }
    
    @Test
    void removeFeedingSchedule_ShouldRemoveFeedingSchedule() {
        // Arrange
        int feedingScheduleId = 1;
        
        // Act
        feedingOrganisationService.removeFeedingSchedule(feedingScheduleId);
        
        // Assert
        verify(feedingScheduleRepository).deleteById(feedingScheduleId);
    }
    
    @Test
    void feed_ShouldProcessAllCurrentTimeSchedules() {
        // Arrange
        LocalTime currentTime = LocalTime.now();
        
        FeedingSchedule schedule1 = new FeedingSchedule(1, 1, currentTime, 5);
        schedule1.setId(1);
        FeedingSchedule schedule2 = new FeedingSchedule(2, 2, currentTime, 3);
        schedule2.setId(2);
        
        List<FeedingSchedule> schedules = Arrays.asList(schedule1, schedule2);
        
        // Use any() matcher for LocalTime to avoid timestamp comparison issues
        when(feedingScheduleRepository.findByFeedingTime(any(LocalTime.class))).thenReturn(schedules);
        
        // Mock successful feeding for first schedule
        Animal animal1 = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        Food food1 = new Food("Meat", 100);
        
        when(feedingScheduleRepository.findById(1)).thenReturn(Optional.of(schedule1));
        when(foodRepository.findById(1)).thenReturn(Optional.of(food1));
        when(animalRepository.findById(1)).thenReturn(Optional.of(animal1));
        
        // Mock exception for second schedule
        when(feedingScheduleRepository.findById(2)).thenReturn(Optional.of(schedule2));
        when(foodRepository.findById(2)).thenReturn(Optional.empty());
        
        // Act
        feedingOrganisationService.feed();
        
        // Assert
        // First schedule should be processed successfully
        assertNotNull(schedule1.getLastFed());
        verify(feedingScheduleRepository).save(schedule1);
        
        // Second schedule should throw exception but be caught internally
        verify(feedingScheduleRepository).findById(2);
        verify(foodRepository).findById(2);
    }
} 