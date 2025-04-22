package com.akanton.kpo.zoorest.infrastructure.repositories;

import com.akanton.kpo.zoorest.domain.Food;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FoodRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a test food
        Food food = new Food("Meat", 100);
        
        // Save the food
        food = entityManager.persistAndFlush(food);
        int foodId = food.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the food by ID
        Optional<Food> foundFood = foodRepository.findById(foodId);
        
        // Verify the food was found and has the correct properties
        assertTrue(foundFood.isPresent());
        assertEquals("Meat", foundFood.get().getName());
        assertEquals(100, foundFood.get().getAmount());
    }
    
    @Test
    public void testFindAll() {
        // Create test foods
        Food food1 = new Food("Hay", 200);
        Food food2 = new Food("Fish", 50);
        Food food3 = new Food("Fruits", 150);
        
        // Save the foods
        entityManager.persist(food1);
        entityManager.persist(food2);
        entityManager.persist(food3);
        entityManager.flush();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find all foods
        List<Food> foods = foodRepository.findAll();
        
        // Verify at least the number of foods we added
        assertTrue(foods.size() >= 3);
        
        // Verify our foods are in the list
        boolean foundHay = false;
        boolean foundFish = false;
        boolean foundFruits = false;
        
        for (Food food : foods) {
            if ("Hay".equals(food.getName())) {
                foundHay = true;
                assertEquals(200, food.getAmount());
            } else if ("Fish".equals(food.getName())) {
                foundFish = true;
                assertEquals(50, food.getAmount());
            } else if ("Fruits".equals(food.getName())) {
                foundFruits = true;
                assertEquals(150, food.getAmount());
            }
        }
        
        assertTrue(foundHay);
        assertTrue(foundFish);
        assertTrue(foundFruits);
    }
    
    @Test
    public void testUpdate() {
        // Create a test food
        Food food = new Food("Vegetables", 75);
        
        // Save the food
        food = entityManager.persistAndFlush(food);
        int foodId = food.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the food, update it, and save it
        Food foundFood = foodRepository.findById(foodId).orElseThrow();
        foundFood.setName("Fresh Vegetables");
        foundFood.setAmount(100);
        foodRepository.save(foundFood);
        
        // Flush to ensure changes are written to the database
        entityManager.flush();
        
        // Clear the persistence context again
        entityManager.clear();
        
        // Find the food again and verify the updates
        Food updatedFood = foodRepository.findById(foodId).orElseThrow();
        assertEquals("Fresh Vegetables", updatedFood.getName());
        assertEquals(100, updatedFood.getAmount());
    }
    
    @Test
    public void testDelete() {
        // Create a test food
        Food food = new Food("Insects", 30);
        
        // Save the food
        food = entityManager.persistAndFlush(food);
        int foodId = food.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Verify the food exists
        assertTrue(foodRepository.existsById(foodId));
        
        // Delete the food
        foodRepository.deleteById(foodId);
        
        // Flush to ensure the delete is written to the database
        entityManager.flush();
        
        // Verify the food no longer exists
        assertFalse(foodRepository.existsById(foodId));
    }
    
    @Test
    public void testInvalidAmount() {
        // Test that negative amount throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            new Food("Invalid Food", -10);
        });
        
        // Test updating to invalid amount
        Food food = new Food("Test Food", 20);
        food = entityManager.persistAndFlush(food);
        
        Food finalFood = food;
        assertThrows(IllegalArgumentException.class, () -> {
            finalFood.setAmount(-5);
        });
    }
} 