package com.akanton.kpo.zoorest.infrastructure.repositories;

import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EnclosureRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnclosureRepository enclosureRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a test enclosure
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 20);
        
        // Save the enclosure
        enclosure = entityManager.persistAndFlush(enclosure);
        int enclosureId = enclosure.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the enclosure by ID
        Optional<Enclosure> foundEnclosure = enclosureRepository.findById(enclosureId);
        
        // Verify the enclosure was found and has the correct properties
        assertTrue(foundEnclosure.isPresent());
        assertEquals(AnimalFoodType.PREDATOR, foundEnclosure.get().getAnimalFoodType());
        assertEquals(AnimalHabitatType.TERRASTRIAL, foundEnclosure.get().getAnimalHabitatType());
        assertEquals(20, foundEnclosure.get().getCapacity());
        assertEquals(0, foundEnclosure.get().getCurrentCount());
    }
    
    @Test
    public void testFindAll() {
        // Create test enclosures
        Enclosure enclosure1 = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.TERRASTRIAL, 15);
        Enclosure enclosure2 = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 10);
        Enclosure enclosure3 = new Enclosure(AnimalFoodType.OMNIVORE, AnimalHabitatType.FLYING, 8);
        
        // Save the enclosures
        entityManager.persist(enclosure1);
        entityManager.persist(enclosure2);
        entityManager.persist(enclosure3);
        entityManager.flush();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find all enclosures
        List<Enclosure> enclosures = enclosureRepository.findAll();
        
        // Verify at least the number of enclosures we added
        assertTrue(enclosures.size() >= 3);
        
        // Verify our enclosures are in the list (by checking unique combinations of properties)
        boolean foundPredTerr = false;
        boolean foundHerbTerr = false;
        boolean foundOmniFly = false;
        
        for (Enclosure enclosure : enclosures) {
            if (AnimalFoodType.PREDATOR.equals(enclosure.getAnimalFoodType()) && 
                AnimalHabitatType.TERRASTRIAL.equals(enclosure.getAnimalHabitatType()) &&
                enclosure.getCapacity() == 15) {
                foundPredTerr = true;
            } else if (AnimalFoodType.HERBIVORE.equals(enclosure.getAnimalFoodType()) && 
                AnimalHabitatType.TERRASTRIAL.equals(enclosure.getAnimalHabitatType()) &&
                enclosure.getCapacity() == 10) {
                foundHerbTerr = true;
            } else if (AnimalFoodType.OMNIVORE.equals(enclosure.getAnimalFoodType()) && 
                AnimalHabitatType.FLYING.equals(enclosure.getAnimalHabitatType()) &&
                enclosure.getCapacity() == 8) {
                foundOmniFly = true;
            }
        }
        
        assertTrue(foundPredTerr);
        assertTrue(foundHerbTerr);
        assertTrue(foundOmniFly);
    }
    
    @Test
    public void testUpdate() {
        // Create a test enclosure
        Enclosure enclosure = new Enclosure(AnimalFoodType.PREDATOR, AnimalHabitatType.FISH, 25);
        
        // Save the enclosure
        enclosure = entityManager.persistAndFlush(enclosure);
        int enclosureId = enclosure.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the enclosure, update it, and save it
        Enclosure foundEnclosure = enclosureRepository.findById(enclosureId).orElseThrow();
        foundEnclosure.setAnimalFoodType(AnimalFoodType.OMNIVORE);
        foundEnclosure.setCapacity(30);
        enclosureRepository.save(foundEnclosure);
        
        // Flush to ensure changes are written to the database
        entityManager.flush();
        
        // Clear the persistence context again
        entityManager.clear();
        
        // Find the enclosure again and verify the updates
        Enclosure updatedEnclosure = enclosureRepository.findById(enclosureId).orElseThrow();
        assertEquals(AnimalFoodType.OMNIVORE, updatedEnclosure.getAnimalFoodType());
        assertEquals(30, updatedEnclosure.getCapacity());
        assertEquals(AnimalHabitatType.FISH, updatedEnclosure.getAnimalHabitatType());
    }
    
    @Test
    public void testDelete() {
        // Create a test enclosure
        Enclosure enclosure = new Enclosure(AnimalFoodType.HERBIVORE, AnimalHabitatType.TERRASTRIAL, 5);
        
        // Save the enclosure
        enclosure = entityManager.persistAndFlush(enclosure);
        int enclosureId = enclosure.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Verify the enclosure exists
        assertTrue(enclosureRepository.existsById(enclosureId));
        
        // Delete the enclosure
        enclosureRepository.deleteById(enclosureId);
        
        // Flush to ensure the delete is written to the database
        entityManager.flush();
        
        // Verify the enclosure no longer exists
        assertFalse(enclosureRepository.existsById(enclosureId));
    }
} 