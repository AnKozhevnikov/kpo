package com.akanton.kpo.zoorest.application.services.interfaces;

import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface IFeedingOrganisationService {
    public void feedAnimal(int feedingScheduleId);
    public void addFeedingSchedule(FeedingSchedule feedingSchedule);
    public void removeFeedingSchedule(int feedingScheduleId);
    public void addFoodAmount(int foodId, int amount);

    @Scheduled(cron = "0 * * * * ?")
    public void feed();
}
