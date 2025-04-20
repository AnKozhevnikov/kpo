package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private IEntityService entityService;
    @Autowired
    private IStatisticsService statisticsService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFood(
            @RequestParam String name,
            @RequestParam int amount
    ) {
        try {
            Food food = new Food(name, amount);
            entityService.addFoodType(food);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Food>> getFoodType() {
        try {
            return ResponseEntity.ok(statisticsService.getAllFood());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable int id) {
        try {
            Food food = statisticsService.getFoodById(id);
            if (food != null) {
                return ResponseEntity.ok(food);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFoodType(@PathVariable int id) {
        try {
            entityService.removeFoodType(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
