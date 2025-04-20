package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import com.akanton.kpo.zoorest.domain.Food;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FeedingScheduleRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService implements IStatisticsService {
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private FeedingScheduleRepository feedingScheduleRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private EnclosureRepository enclosureRepository;

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }

    public List<FeedingSchedule> getAllFeedingSchedules() {
        return feedingScheduleRepository.findAll();
    }

    public List<Enclosure> getAllEnclosures() {
        return enclosureRepository.findAll();
    }

    public Animal getAnimalById(int id) {
        return animalRepository.findById(id).orElse(null);
    }

    public Food getFoodById(int id) {
        return foodRepository.findById(id).orElse(null);
    }

    public FeedingSchedule getFeedingScheduleById(int id) {
        return feedingScheduleRepository.findById(id).orElse(null);
    }

    public Enclosure getEnclosureById(int id) {
        return enclosureRepository.findById(id).orElse(null);
    }
}
