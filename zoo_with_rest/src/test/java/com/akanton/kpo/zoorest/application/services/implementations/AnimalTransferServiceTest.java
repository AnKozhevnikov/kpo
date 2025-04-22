package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.events.AnimalMovedEvent;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.SexType;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalTransferServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private EnclosureRepository enclosureRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AnimalTransferService animalTransferService;

    @Captor
    private ArgumentCaptor<AnimalMovedEvent> eventCaptor;

    @Test
    void transferAnimal_ShouldTransferAnimalAndPublishEvent_WhenAllConditionsAreMet() {
        // Arrange
        int animalId = 1;
        int oldEnclosureId = 1;
        int newEnclosureId = 2;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, oldEnclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        animal.setId(animalId);
        
        Enclosure oldEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        oldEnclosure.setId(oldEnclosureId);
        oldEnclosure.addAnimal(); // Current count = 1
        
        Enclosure newEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        newEnclosure.setId(newEnclosureId);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(oldEnclosureId)).thenReturn(Optional.of(oldEnclosure));
        when(enclosureRepository.findById(newEnclosureId)).thenReturn(Optional.of(newEnclosure));
        
        // Act
        animalTransferService.transferAnimal(animalId, newEnclosureId);
        
        // Assert
        assertEquals(newEnclosureId, animal.getEnclosureId());
        assertEquals(0, oldEnclosure.getCurrentCount());
        assertEquals(1, newEnclosure.getCurrentCount());
        
        verify(animalRepository).save(animal);
        verify(enclosureRepository).save(oldEnclosure);
        verify(enclosureRepository).save(newEnclosure);
        
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        AnimalMovedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(animalId, capturedEvent.getAnimalId());
        
        assertEquals(newEnclosureId, capturedEvent.getFromEnclosureId());
        assertEquals(newEnclosureId, capturedEvent.getToEnclosureId());
    }
    
    @Test
    void transferAnimal_ShouldThrowException_WhenAnimalDoesNotExist() {
        // Arrange
        int animalId = 1;
        int newEnclosureId = 2;
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
        });
        
        assertEquals("Animal not found", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository, eventPublisher);
    }
    
    @Test
    void transferAnimal_ShouldThrowException_WhenOldEnclosureDoesNotExist() {
        // Arrange
        int animalId = 1;
        int oldEnclosureId = 1;
        int newEnclosureId = 2;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, oldEnclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(oldEnclosureId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
        });
        
        assertEquals("Old enclosure not found", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verify(enclosureRepository).findById(oldEnclosureId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository, eventPublisher);
    }
    
    @Test
    void transferAnimal_ShouldThrowException_WhenNewEnclosureDoesNotExist() {
        // Arrange
        int animalId = 1;
        int oldEnclosureId = 1;
        int newEnclosureId = 2;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, oldEnclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure oldEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        oldEnclosure.setId(oldEnclosureId);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(oldEnclosureId)).thenReturn(Optional.of(oldEnclosure));
        when(enclosureRepository.findById(newEnclosureId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
        });
        
        assertEquals("New enclosure not found", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verify(enclosureRepository).findById(oldEnclosureId);
        verify(enclosureRepository).findById(newEnclosureId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository, eventPublisher);
    }
    
    @Test
    void transferAnimal_ShouldThrowException_WhenFoodTypesDoNotMatch() {
        // Arrange
        int animalId = 1;
        int oldEnclosureId = 1;
        int newEnclosureId = 2;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, oldEnclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure oldEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        oldEnclosure.setId(oldEnclosureId);
        
        Enclosure newEnclosure = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 10);
        newEnclosure.setId(newEnclosureId);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(oldEnclosureId)).thenReturn(Optional.of(oldEnclosure));
        when(enclosureRepository.findById(newEnclosureId)).thenReturn(Optional.of(newEnclosure));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
        });
        
        assertEquals("Animal food type does not match enclosure food type", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verify(enclosureRepository).findById(oldEnclosureId);
        verify(enclosureRepository).findById(newEnclosureId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository, eventPublisher);
    }
    
    @Test
    void transferAnimal_ShouldThrowException_WhenHabitatTypesDoNotMatch() {
        // Arrange
        int animalId = 1;
        int oldEnclosureId = 1;
        int newEnclosureId = 2;
        
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, oldEnclosureId, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        Enclosure oldEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        oldEnclosure.setId(oldEnclosureId);
        
        Enclosure newEnclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.FISH, 10);
        newEnclosure.setId(newEnclosureId);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(oldEnclosureId)).thenReturn(Optional.of(oldEnclosure));
        when(enclosureRepository.findById(newEnclosureId)).thenReturn(Optional.of(newEnclosure));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
        });
        
        assertEquals("Animal habitat type does not match enclosure habitat type", exception.getMessage());
        verify(animalRepository).findById(animalId);
        verify(enclosureRepository).findById(oldEnclosureId);
        verify(enclosureRepository).findById(newEnclosureId);
        verifyNoMoreInteractions(animalRepository, enclosureRepository, eventPublisher);
    }
} 