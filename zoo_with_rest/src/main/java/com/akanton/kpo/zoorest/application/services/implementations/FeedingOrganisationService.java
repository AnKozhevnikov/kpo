package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.events.FeedingTimeEvent;
import com.akanton.kpo.zoorest.application.services.interfaces.IFeedingOrganisationService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import com.akanton.kpo.zoorest.domain.Food;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FeedingScheduleRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Service
public class FeedingOrganisationService implements IFeedingOrganisationService {
    @Autowired
    FeedingScheduleRepository feedingScheduleRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    AnimalRepository animalRepository;

    public void feedAnimal(int feedingScheduleId) {
        FeedingSchedule feedingSchedule = feedingScheduleRepository.findById(feedingScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Feeding schedule not found"));
        Food food = foodRepository.findById(feedingSchedule.getFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Food not found"));
        Animal animal = animalRepository.findById(feedingSchedule.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));

        if (feedingSchedule.getAmount() > food.getAmount()) {
            throw new IllegalArgumentException("Not enough food available");
        }

        OffsetDateTime feedingTime = OffsetDateTime.now();

        food.setAmount(food.getAmount() - feedingSchedule.getAmount());
        foodRepository.save(food);

        feedingSchedule.markFed(feedingTime);
        feedingScheduleRepository.save(feedingSchedule);

        animal.feed(feedingTime);
        animalRepository.save(animal);

        FeedingTimeEvent event = new FeedingTimeEvent(this, feedingScheduleId, feedingTime);
    }

    public void addFeedingSchedule(FeedingSchedule feedingSchedule) {
        feedingScheduleRepository.save(feedingSchedule);
    }

    public void addFoodAmount(int foodId, int amount) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found"));
        food.setAmount(food.getAmount() + amount);
        foodRepository.save(food);
    }

    public void removeFeedingSchedule(int feedingScheduleId) {
        feedingScheduleRepository.deleteById(feedingScheduleId);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void feed() {
        List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.findByFeedingTime(LocalTime.now());
        for (FeedingSchedule feedingSchedule : feedingSchedules) {
            try {
                feedAnimal(feedingSchedule.getId());
            } catch (IllegalArgumentException e) {
                System.out.println("Error feeding animal: " + e.getMessage());
            }
        }
    }
}
