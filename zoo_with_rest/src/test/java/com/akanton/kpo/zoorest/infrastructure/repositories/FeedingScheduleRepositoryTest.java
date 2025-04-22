package com.akanton.kpo.zoorest.infrastructure.repositories;

import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FeedingScheduleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeedingScheduleRepository feedingScheduleRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a test feeding schedule
        LocalTime feedingTime = LocalTime.of(8, 0);
        FeedingSchedule schedule = new FeedingSchedule(1, 1, feedingTime, 5);
        
        // Save the schedule
        schedule = entityManager.persistAndFlush(schedule);
        int scheduleId = schedule.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the schedule by ID
        Optional<FeedingSchedule> foundSchedule = feedingScheduleRepository.findById(scheduleId);
        
        // Verify the schedule was found and has the correct properties
        assertTrue(foundSchedule.isPresent());
        assertEquals(1, foundSchedule.get().getAnimalId());
        assertEquals(1, foundSchedule.get().getFoodId());
        assertEquals(feedingTime, foundSchedule.get().getFeedingTime());
        assertEquals(5, foundSchedule.get().getAmount());
    }
    
    @Test
    public void testFindByFoodId() {
        // Create test feeding schedules with different food IDs
        FeedingSchedule schedule1 = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        FeedingSchedule schedule2 = new FeedingSchedule(2, 1, LocalTime.of(12, 0), 3);
        FeedingSchedule schedule3 = new FeedingSchedule(3, 2, LocalTime.of(16, 0), 4);
        
        // Save the schedules
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(schedule3);
        entityManager.flush();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find schedules by food ID
        List<FeedingSchedule> schedulesWithFood1 = feedingScheduleRepository.findByFoodId(1);
        List<FeedingSchedule> schedulesWithFood2 = feedingScheduleRepository.findByFoodId(2);
        List<FeedingSchedule> schedulesWithFood3 = feedingScheduleRepository.findByFoodId(3);
        
        // Verify the results
        assertEquals(2, schedulesWithFood1.size());
        assertEquals(1, schedulesWithFood2.size());
        assertEquals(0, schedulesWithFood3.size());
    }
    
    @Test
    public void testFindByFeedingTime() {
        // Create test feeding schedules with different feeding times
        LocalTime morningTime = LocalTime.of(8, 0);
        LocalTime noonTime = LocalTime.of(12, 0);
        
        FeedingSchedule schedule1 = new FeedingSchedule(1, 1, morningTime, 5);
        FeedingSchedule schedule2 = new FeedingSchedule(2, 2, morningTime, 3);
        FeedingSchedule schedule3 = new FeedingSchedule(3, 3, noonTime, 4);
        
        // Save the schedules
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(schedule3);
        entityManager.flush();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find schedules by feeding time
        List<FeedingSchedule> morningSchedules = feedingScheduleRepository.findByFeedingTime(morningTime);
        List<FeedingSchedule> noonSchedules = feedingScheduleRepository.findByFeedingTime(noonTime);
        List<FeedingSchedule> eveningSchedules = feedingScheduleRepository.findByFeedingTime(LocalTime.of(18, 0));
        
        // Verify the results
        assertEquals(2, morningSchedules.size());
        assertEquals(1, noonSchedules.size());
        assertEquals(0, eveningSchedules.size());
    }
    
    @Test
    public void testUpdate() {
        // Create a test feeding schedule
        LocalTime feedingTime = LocalTime.of(8, 0);
        FeedingSchedule schedule = new FeedingSchedule(1, 1, feedingTime, 5);
        
        // Save the schedule
        schedule = entityManager.persistAndFlush(schedule);
        int scheduleId = schedule.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the schedule, update it, and save it
        FeedingSchedule foundSchedule = feedingScheduleRepository.findById(scheduleId).orElseThrow();
        foundSchedule.setFeedingTime(LocalTime.of(9, 30));
        foundSchedule.setAmount(7);
        
        // Save and flush to ensure changes are written to the database
        feedingScheduleRepository.save(foundSchedule);
        entityManager.flush();
        
        // Clear the persistence context again
        entityManager.clear();
        
        // Find the schedule again and verify the updates
        FeedingSchedule updatedSchedule = feedingScheduleRepository.findById(scheduleId).orElseThrow();
        assertEquals(LocalTime.of(9, 30), updatedSchedule.getFeedingTime());
        assertEquals(7, updatedSchedule.getAmount());
        assertEquals(1, updatedSchedule.getAnimalId());
    }
    
    @Test
    public void testDelete() {
        // Create a test feeding schedule
        FeedingSchedule schedule = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        
        // Save the schedule
        schedule = entityManager.persistAndFlush(schedule);
        int scheduleId = schedule.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Verify the schedule exists
        assertTrue(feedingScheduleRepository.existsById(scheduleId));
        
        // Delete the schedule
        feedingScheduleRepository.deleteById(scheduleId);
        
        // Flush to ensure the delete is written to the database
        entityManager.flush();
        
        // Verify the schedule no longer exists
        assertFalse(feedingScheduleRepository.existsById(scheduleId));
    }
    
    @Test
    public void testMarkFed() {
        // Create a test feeding schedule
        FeedingSchedule schedule = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        
        // Save the schedule
        schedule = entityManager.persistAndFlush(schedule);
        int scheduleId = schedule.getId();
        
        // Clear the persistence context
        entityManager.clear();
        
        // Find the schedule, mark it as fed, and save it
        FeedingSchedule foundSchedule = feedingScheduleRepository.findById(scheduleId).orElseThrow();
        OffsetDateTime fedTime = OffsetDateTime.now();
        foundSchedule.markFed(fedTime);
        
        // Save and flush to ensure changes are written to the database
        feedingScheduleRepository.save(foundSchedule);
        entityManager.flush();
        
        // Clear the persistence context again
        entityManager.clear();
        
        // Find the schedule again and verify it was marked as fed
        FeedingSchedule updatedSchedule = feedingScheduleRepository.findById(scheduleId).orElseThrow();
        assertNotNull(updatedSchedule.getLastFed());
        // We can't directly compare the timestamps as they might have slight differences in precision
        // Instead, check that the timestamps are close (within 10 seconds)
        long timeDifference = Math.abs(updatedSchedule.getLastFed().toEpochSecond() - fedTime.toEpochSecond());
        assertTrue(timeDifference < 10);
    }
    
    @Test
    public void testInvalidAmount() {
        // Test that negative amount throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            new FeedingSchedule(1, 1, LocalTime.of(8, 0), -5);
        });
        
        // Test updating to invalid amount
        FeedingSchedule schedule = new FeedingSchedule(1, 1, LocalTime.of(8, 0), 5);
        schedule = entityManager.persistAndFlush(schedule);
        
        FeedingSchedule finalSchedule = schedule;
        assertThrows(IllegalArgumentException.class, () -> {
            finalSchedule.setAmount(-3);
        });
    }
} 