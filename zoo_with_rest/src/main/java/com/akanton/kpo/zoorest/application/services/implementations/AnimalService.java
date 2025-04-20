package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService implements IAnimalService {
    @Autowired
    private AnimalRepository animalRepository;

    public void healAnimal(int animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
        animal.heal();
        animalRepository.save(animal);
    }
}
