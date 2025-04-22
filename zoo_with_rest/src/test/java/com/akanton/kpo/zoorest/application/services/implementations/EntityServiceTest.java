package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.Food;
import com.akanton.kpo.zoorest.domain.SexType;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FeedingScheduleRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private EnclosureRepository enclosureRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FeedingScheduleRepository feedingScheduleRepository;

    @InjectMocks
    private EntityService entityService;

    @Test
    void addAnimal_ShouldAddAnimal_WhenAllConditionsAreMet() {
        // Arrange
        int enclosureId = 1;
        int foodId = 1;
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        Food food = new Food("Meat", 100);
        food.setId(foodId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        
        // Act
        entityService.addAnimal(animal);
        
        // Assert
        assertEquals(1, enclosure.getCurrentCount());
        verify(enclosureRepository).save(enclosure);
        verify(animalRepository).save(animal);
    }
    
    @Test
    void addAnimal_ShouldThrowException_WhenEnclosureNotFound() {
        // Arrange
        int enclosureId = 999;
        int foodId = 1;
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.addAnimal(animal);
        });
        
        assertEquals("Enclosure not found", exception.getMessage());
        verify(enclosureRepository).findById(enclosureId);
        verifyNoMoreInteractions(enclosureRepository, animalRepository, foodRepository);
    }
    
    @Test
    void addAnimal_ShouldThrowException_WhenFoodTypeDoesNotMatch() {
        // Arrange
        int enclosureId = 1;
        int foodId = 1;
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure enclosure = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.addAnimal(animal);
        });
        
        assertEquals("Animal food type does not match enclosure food type", exception.getMessage());
        verify(enclosureRepository).findById(enclosureId);
        verifyNoMoreInteractions(enclosureRepository, animalRepository, foodRepository);
    }
    
    @Test
    void addAnimal_ShouldThrowException_WhenHabitatTypeDoesNotMatch() {
        // Arrange
        int enclosureId = 1;
        int foodId = 1;
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.FISH, 10);
        enclosure.setId(enclosureId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.addAnimal(animal);
        });
        
        assertEquals("Animal habitat type does not match enclosure habitat type", exception.getMessage());
        verify(enclosureRepository).findById(enclosureId);
        verifyNoMoreInteractions(enclosureRepository, animalRepository, foodRepository);
    }
    
    @Test
    void addAnimal_ShouldThrowException_WhenFoodNotFound() {
        // Arrange
        int enclosureId = 1;
        int foodId = 999;
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), foodId, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.addAnimal(animal);
        });
        
        assertEquals("Food not found", exception.getMessage());
        verify(enclosureRepository).findById(enclosureId);
        verify(foodRepository).findById(foodId);
        verifyNoMoreInteractions(enclosureRepository, animalRepository, foodRepository);
    }
    
    @Test
    void addEnclosure_ShouldSaveEnclosure() {
        // Arrange
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        
        // Act
        entityService.addEnclosure(enclosure);
        
        // Assert
        verify(enclosureRepository).save(enclosure);
    }
    
    @Test
    void addFoodType_ShouldSaveFood() {
        // Arrange
        Food food = new Food("Meat", 100);
        
        // Act
        entityService.addFoodType(food);
        
        // Assert
        verify(foodRepository).save(food);
    }
    
    @Test
    void removeAnimal_ShouldRemoveAnimal_WhenAllConditionsAreMet() {
        // Arrange
        int animalId = 1;
        int enclosureId = 1;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        animal.setId(animalId);
        
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        enclosure.addAnimal(); // Current count = 1
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act
        entityService.removeAnimal(animalId);
        
        // Assert
        assertEquals(0, enclosure.getCurrentCount());
        verify(enclosureRepository).save(enclosure);
        verify(animalRepository).delete(animal);
    }
    
    @Test
    void removeAnimal_ShouldThrowException_WhenAnimalNotFound() {
        // Arrange
        int animalId = 999;
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.removeAnimal(animalId);
        });
        
        assertEquals("Animal not found", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository);
    }
    
    @Test
    void removeAnimal_ShouldThrowException_WhenEnclosureNotFound() {
        // Arrange
        int animalId = 1;
        int enclosureId = 999;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, enclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        animal.setId(animalId);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            entityService.removeAnimal(animalId);
        });
        
        assertEquals("Enclosure not found", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verify(enclosureRepository).findById(enclosureId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository);
    }
    
    @Test
    void removeFoodType_ShouldRemoveFood() {
        // Arrange
        int foodId = 1;
        Food food = new Food("Meat", 10);
        food.setId(foodId);
        
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(animalRepository.findByFavouriteFoodId(foodId)).thenReturn(Collections.emptyList());
        when(feedingScheduleRepository.findByFoodId(foodId)).thenReturn(Collections.emptyList());
        
        // Act
        entityService.removeFoodType(foodId);
        
        // Assert
        verify(foodRepository).delete(food);
    }
    
    @Test
    void removeEnclosure_ShouldRemoveEnclosure() {
        // Arrange
        int enclosureId = 1;
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act
        entityService.removeEnclosure(enclosureId);
        
        // Assert
        verify(enclosureRepository).delete(enclosure);
    }
} 