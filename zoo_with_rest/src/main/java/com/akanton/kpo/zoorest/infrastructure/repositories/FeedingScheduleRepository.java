package com.akanton.kpo.zoorest.infrastructure.repositories;

import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface FeedingScheduleRepository extends JpaRepository<FeedingSchedule, Integer> {
    List<FeedingSchedule> findByFoodId(Integer foodId);
    List<FeedingSchedule> findByFeedingTime(LocalTime feedingTime);
}
