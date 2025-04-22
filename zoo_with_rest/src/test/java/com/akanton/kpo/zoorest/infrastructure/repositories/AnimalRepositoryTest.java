package com.akanton.kpo.zoorest.infrastructure.repositories;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.SexType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnimalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a test animal
        Animal animal = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 1, true, 
                                  AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        
        // Save the animal
        animal = entityManager.persistAndFlush(animal);
        int animalId = animal.getId();
        
        // Clear the persistence context to force a fresh read from the database
        entityManager.clear();
        
        // Find the animal by ID
        Optional<Animal> foundAnimal = animalRepository.findById(animalId);
        
        // Verify the animal was found and has the correct properties
        assertTrue(foundAnimal.isPresent());
        assertEquals("Lion", foundAnimal.get().getName());
        assertEquals(SexType.MALE, foundAnimal.get().getSex());
        assertEquals(AnimalFoodType.PREDATOR, foundAnimal.get().getAnimalFoodType());
    }
    
    @Test
    public void testFindByFavouriteFoodId() {
        // Create test animals with different favorite foods
        Animal animal1 = new Animal("Tiger", SexType.FEMALE, OffsetDateTime.now(), 1, 1, true, 
                                   AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        Animal animal2 = new Animal("Lion", SexType.MALE, OffsetDateTime.now(), 1, 2, true, 
                                   AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL);
        Animal animal3 = new Animal("Giraffe", SexType.FEMALE, OffsetDateTime.now(), 2, 3, true, 
                                   AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL);
        
        // Save the animals
        entityManager.persist(animal1);
        entityManager.persist(animal2);
        entityManager.persist(animal3);
        entityManager.flush();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find animals by favourite food ID
        List<Animal> animalsWithFood1 = animalRepository.findByFavouriteFoodId(1);
        List<Animal> animalsWithFood2 = animalRepository.findByFavouriteFoodId(2);
        List<Animal> animalsWithFood3 = animalRepository.findByFavouriteFoodId(3);
        
        // Verify the results
        assertEquals(2, animalsWithFood1.size());
        assertEquals(1, animalsWithFood2.size());
        assertEquals(0, animalsWithFood3.size());
    }
    
    @Test
    public void testUpdate() {
        // Create a test animal
        Animal animal = new Animal("Elephant", SexType.MALE, OffsetDateTime.now(), 2, 3, true, 
                                  AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL);
        
        // Save the animal
        animal = entityManager.persistAndFlush(animal);
        int animalId = animal.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the animal, update it, and save it
        Animal foundAnimal = animalRepository.findById(animalId).orElseThrow();
        foundAnimal.setName("African Elephant");
        foundAnimal.setHealthy(false);
        animalRepository.save(foundAnimal);
        
        // Flush to ensure changes are written to the database
        entityManager.flush();
        
        // Clear the persistence context again
        entityManager.clear();
        
        // Find the animal again and verify the updates
        Animal updatedAnimal = animalRepository.findById(animalId).orElseThrow();
        assertEquals("African Elephant", updatedAnimal.getName());
        assertFalse(updatedAnimal.isHealthy());
    }
    
    @Test
    public void testDelete() {
        // Create a test animal
        Animal animal = new Animal("Zebra", SexType.FEMALE, OffsetDateTime.now(), 2, 3, true, 
                                  AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL);
        
        // Save the animal
        animal = entityManager.persistAndFlush(animal);
        int animalId = animal.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Verify the animal exists
        assertTrue(animalRepository.existsById(animalId));
        
        // Delete the animal
        animalRepository.deleteById(animalId);
        
        // Flush to ensure the delete is written to the database
        entityManager.flush();
        
        // Verify the animal no longer exists
        assertFalse(animalRepository.existsById(animalId));
    }
} 