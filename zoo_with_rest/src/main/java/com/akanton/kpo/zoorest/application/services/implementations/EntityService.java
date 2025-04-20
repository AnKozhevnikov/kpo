package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
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
public class EntityService implements IEntityService {
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EnclosureRepository enclosureRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FeedingScheduleRepository feedingScheduleRepository;

    public void addAnimal(Animal animal) {
        Enclosure enclosure = enclosureRepository.findById(animal.getEnclosureId())
                .orElseThrow(() -> new IllegalArgumentException("Enclosure not found"));
        if (enclosure.getAnimalFoodType() != animal.getAnimalFoodType()) {
            throw new IllegalArgumentException("Animal food type does not match enclosure food type");
        }
        if (enclosure.getAnimalHabitatType() != animal.getAnimalHabitatType()) {
            throw new IllegalArgumentException("Animal habitat type does not match enclosure habitat type");
        }
        Food food = foodRepository.findById(animal.getFavouriteFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Food not found"));
        enclosure.addAnimal();
        enclosureRepository.save(enclosure);
        animalRepository.save(animal);
    }

    public void addEnclosure(Enclosure enclosure) {
        enclosureRepository.save(enclosure);
    }

    public void addFoodType(Food food) {
        foodRepository.save(food);
    }

    public void removeAnimal(int animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
        Enclosure enclosure = enclosureRepository.findById(animal.getEnclosureId())
                .orElseThrow(() -> new IllegalArgumentException("Enclosure not found"));
        enclosure.removeAnimal();
        enclosureRepository.save(enclosure);
        animalRepository.delete(animal);
    }

    public void removeEnclosure(int enclosureId) {
        Enclosure enclosure = enclosureRepository.findById(enclosureId)
                .orElseThrow(() -> new IllegalArgumentException("Enclosure not found"));
        if (enclosure.getCurrentCount() > 0) {
            throw new IllegalArgumentException("Cannot remove enclosure with animals");
        }
        enclosureRepository.delete(enclosure);
    }

    public void removeFoodType(int foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found"));
        List<Animal> animals = animalRepository.findByFavouriteFoodId(foodId);
        List<FeedingSchedule> schedules = feedingScheduleRepository.findByFoodId(foodId);
        if (!animals.isEmpty() || !schedules.isEmpty()) {
            throw new IllegalArgumentException("Cannot remove food type that is in use");
        }
        foodRepository.delete(food);
    }
}
