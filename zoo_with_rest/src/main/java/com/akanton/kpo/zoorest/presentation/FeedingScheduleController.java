package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IFeedingOrganisationService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/feeding_schedule")
public class FeedingScheduleController {
    @Autowired
    private IFeedingOrganisationService feedingOrganisationService;
    @Autowired
    private IStatisticsService statisticsService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFeedingSchedule(
            @RequestParam int animalId,
            @RequestParam int foodId,
            @RequestParam LocalTime feedingTime,
            @RequestParam int amount
    ) {
        try {
            FeedingSchedule feedingSchedule = new FeedingSchedule(
                    animalId,
                    foodId,
                    feedingTime,
                    amount
            );
            feedingOrganisationService.addFeedingSchedule(feedingSchedule);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFeedingSchedule(@PathVariable int id) {
        try {
            feedingOrganisationService.removeFeedingSchedule(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FeedingSchedule>> getAllFeedingSchedules() {
        try {
            return ResponseEntity.ok(statisticsService.getAllFeedingSchedules());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedingSchedule> getFeedingScheduleById(@PathVariable int id) {
        try {
            FeedingSchedule schedule = statisticsService.getFeedingScheduleById(id);
            if (schedule != null) {
                return ResponseEntity.ok(schedule);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
