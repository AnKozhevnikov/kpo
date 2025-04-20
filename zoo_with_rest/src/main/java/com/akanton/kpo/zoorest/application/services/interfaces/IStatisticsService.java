package com.akanton.kpo.zoorest.application.services.interfaces;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import com.akanton.kpo.zoorest.domain.Food;

import java.util.List;

public interface IStatisticsService {
    public List<Animal> getAllAnimals();
    public List<Food> getAllFood();
    public List<FeedingSchedule> getAllFeedingSchedules();
    public List<Enclosure> getAllEnclosures();

    public Animal getAnimalById(int id);
    public Food getFoodById(int id);
    public FeedingSchedule getFeedingScheduleById(int id);
    public Enclosure getEnclosureById(int id);
}
