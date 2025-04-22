package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.SexType;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void healAnimal_ShouldHealAnimal_WhenAnimalExists() {
        // Arrange
        int animalId = 1;
        Animal animal = new Animal("Sick Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, false,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        
        // Act
        animalService.healAnimal(animalId);
        
        // Assert
        verify(animalRepository).findById(animalId);
        verify(animalRepository).save(animal);
        assert(animal.isHealthy());
    }

    @Test
    void healAnimal_ShouldThrowException_WhenAnimalDoesNotExist() {
        // Arrange
        int animalId = 1;
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        
        // Act & Assert
        try {
            animalService.healAnimal(animalId);
        } catch (RuntimeException e) {
            assert(e.getMessage().equals("Animal not found"));
        }
        
        verify(animalRepository).findById(animalId);
        verify(animalRepository, never()).save(any());
    }
} 