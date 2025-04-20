package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalService;
import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalTransferService;
import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.AnimalFoodType;
import com.akanton.kpo.zoorest.domain.AnimalHabitatType;
import com.akanton.kpo.zoorest.domain.SexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    @Autowired
    private IEntityService entityService;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IAnimalTransferService animalTransferService;

    @Autowired
    private IAnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        try {
            return ResponseEntity.ok(statisticsService.getAllAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable int id) {
        try {
            Animal animal = statisticsService.getAnimalById(id);
            if (animal != null) {
                return ResponseEntity.ok(animal);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addAnimal(
            @RequestParam String name,
            @RequestParam SexType sex,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime birthDate,
            @RequestParam int favouriteFoodId,
            @RequestParam int enclosureId,
            @RequestParam boolean healthy,
            @RequestParam AnimalFoodType animalFoodType,
            @RequestParam AnimalHabitatType animalHabitatType
    ) {
        try {
            Animal animal = new Animal(
                    name,
                    sex,
                    birthDate,
                    favouriteFoodId,
                    enclosureId,
                    healthy,
                    animalFoodType,
                    animalHabitatType
            );
            entityService.addAnimal(animal);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<Void> transferAnimal(@RequestParam int animalId, @RequestParam int newEnclosureId) {
        try {
            animalTransferService.transferAnimal(animalId, newEnclosureId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/heal")
    public ResponseEntity<Void> healAnimal(@RequestParam int animalId) {
        try {
            animalService.healAnimal(animalId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAnimal(@PathVariable int id) {
        try {
            entityService.removeAnimal(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
