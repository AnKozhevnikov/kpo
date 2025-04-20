package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEnclosureService;
import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.Enclosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enclosures")
public class EnclosureController {
    @Autowired
    private IEntityService entityService;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IEnclosureService enclosureService;

    @GetMapping
    public ResponseEntity<List<Enclosure>> getAllEnclosures() {
        try {
            return ResponseEntity.ok(statisticsService.getAllEnclosures());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enclosure> getEnclosureById(@PathVariable int id) {
        try {
            Enclosure enclosure = statisticsService.getEnclosureById(id);
            if (enclosure != null) {
                return ResponseEntity.ok(enclosure);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addEnclosure(
            @RequestParam AnimalFoodType animalFoodType,
            @RequestParam AnimalHabitatType animalHabitatType,
            @RequestParam int capacity
    ) {
        try {
            Enclosure enclosure = new Enclosure(
                    animalFoodType,
                    animalHabitatType,
                    capacity
            );
            entityService.addEnclosure(enclosure);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/clean")
    public ResponseEntity<Void> cleanEnclosure(@RequestParam int enclosureId) {
        try {
            enclosureService.cleanEnclosure(enclosureId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeEnclosure(@PathVariable int id) {
        try {
            entityService.removeEnclosure(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
