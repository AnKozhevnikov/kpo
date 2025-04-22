package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnclosureServiceTest {

    @Mock
    private EnclosureRepository enclosureRepository;

    @InjectMocks
    private EnclosureService enclosureService;

    @Test
    void cleanEnclosure_ShouldCleanEnclosure_WhenEnclosureExists() {
        // Arrange
        int enclosureId = 1;
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act
        enclosureService.cleanEnclosure(enclosureId);
        
        // Assert
        assertNotNull(enclosure.getLastCleaned());
        verify(enclosureRepository).findById(enclosureId);
        verify(enclosureRepository).save(enclosure);
    }

    @Test
    void cleanEnclosure_ShouldThrowException_WhenEnclosureDoesNotExist() {
        // Arrange
        int enclosureId = 999;
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enclosureService.cleanEnclosure(enclosureId);
        });
        
        assertEquals("Enclosure not found", exception.getMessage());
        verify(enclosureRepository).findById(enclosureId);
        verifyNoMoreInteractions(enclosureRepository);
    }
    
    @Test
    void cleanEnclosure_ShouldUpdateLastCleanedTime() {
        // Arrange
        int enclosureId = 1;
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        enclosure.setId(enclosureId);
        OffsetDateTime beforeCleaning = OffsetDateTime.now().minusHours(1);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act
        enclosureService.cleanEnclosure(enclosureId);
        
        // Assert
        assertTrue(enclosure.getLastCleaned().isAfter(beforeCleaning));
        verify(enclosureRepository).save(enclosure);
    }
} 