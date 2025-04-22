package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
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

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private EnclosureRepository enclosureRepository;

    @Mock
    private FeedingScheduleRepository feedingScheduleRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void getAllAnimals_ShouldReturnAllAnimals() {
        // Arrange
        Animal lion = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        Animal tiger = new Animal("Tiger", SexType.FEMALE, OffsetDateTime.now(), 2, 2, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        List<Animal> animals = Arrays.asList(lion, tiger);
        
        when(animalRepository.findAll()).thenReturn(animals);
        
        // Act
        List<Animal> result = statisticsService.getAllAnimals();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Lion", result.get(0).getName());
        assertEquals("Tiger", result.get(1).getName());
    }
    
    @Test
    void getAllFood_ShouldReturnAllFood() {
        // Arrange
        Food meat = new Food("Meat", 100);
        Food hay = new Food("Hay", 200);
        
        List<Food> foods = Arrays.asList(meat, hay);
        
        when(foodRepository.findAll()).thenReturn(foods);
        
        // Act
        List<Food> result = statisticsService.getAllFood();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Meat", result.get(0).getName());
        assertEquals("Hay", result.get(1).getName());
    }
    
    @Test
    void getAllFeedingSchedules_ShouldReturnAllSchedules() {
        // Arrange
        FeedingSchedule schedule1 = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        FeedingSchedule schedule2 = new FeedingSchedule(2, 2, LocalTime.of(12, 0), 3);
        
        List<FeedingSchedule> schedules = Arrays.asList(schedule1, schedule2);
        
        when(feedingScheduleRepository.findAll()).thenReturn(schedules);
        
        // Act
        List<FeedingSchedule> result = statisticsService.getAllFeedingSchedules();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getAnimalId());
        assertEquals(2, result.get(1).getAnimalId());
    }
    
    @Test
    void getAllEnclosures_ShouldReturnAllEnclosures() {
        // Arrange
        Enclosure enclosure1 = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        Enclosure enclosure2 = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 20);
        
        List<Enclosure> enclosures = Arrays.asList(enclosure1, enclosure2);
        
        when(enclosureRepository.findAll()).thenReturn(enclosures);
        
        // Act
        List<Enclosure> result = statisticsService.getAllEnclosures();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(AnimalFoodType.PREDATOR, result.get(0).getAnimalFoodType());
        assertEquals(AnimalFoodType.HERBIVORE, result.get(1).getAnimalFoodType());
    }
    
    @Test
    void getAnimalById_WithExistingId_ShouldReturnAnimal() {
        // Arrange
        int animalId = 1;
        Animal lion = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true,
                AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(lion));
        
        // Act
        Animal result = statisticsService.getAnimalById(animalId);
        
        // Assert
        assertEquals("Lion", result.getName());
    }
    
    @Test
    void getAnimalById_WithNonExistingId_ShouldReturnNull() {
        // Arrange
        int animalId = 999;
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        
        // Act
        Animal result = statisticsService.getAnimalById(animalId);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void getFoodById_WithExistingId_ShouldReturnFood() {
        // Arrange
        int foodId = 1;
        Food meat = new Food("Meat", 100);
        
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(meat));
        
        // Act
        Food result = statisticsService.getFoodById(foodId);
        
        // Assert
        assertEquals("Meat", result.getName());
    }
    
    @Test
    void getFoodById_WithNonExistingId_ShouldReturnNull() {
        // Arrange
        int foodId = 999;
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());
        
        // Act
        Food result = statisticsService.getFoodById(foodId);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void getFeedingScheduleById_WithExistingId_ShouldReturnSchedule() {
        // Arrange
        int scheduleId = 1;
        FeedingSchedule schedule = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        
        when(feedingScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        
        // Act
        FeedingSchedule result = statisticsService.getFeedingScheduleById(scheduleId);
        
        // Assert
        assertEquals(1, result.getAnimalId());
    }
    
    @Test
    void getFeedingScheduleById_WithNonExistingId_ShouldReturnNull() {
        // Arrange
        int scheduleId = 999;
        when(feedingScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
        
        // Act
        FeedingSchedule result = statisticsService.getFeedingScheduleById(scheduleId);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void getEnclosureById_WithExistingId_ShouldReturnEnclosure() {
        // Arrange
        int enclosureId = 1;
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 10);
        
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(enclosure));
        
        // Act
        Enclosure result = statisticsService.getEnclosureById(enclosureId);
        
        // Assert
        assertEquals(AnimalFoodType.PREDATOR, result.getAnimalFoodType());
    }
    
    @Test
    void getEnclosureById_WithNonExistingId_ShouldReturnNull() {
        // Arrange
        int enclosureId = 999;
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.empty());
        
        // Act
        Enclosure result = statisticsService.getEnclosureById(enclosureId);
        
        // Assert
        assertNull(result);
    }
} 