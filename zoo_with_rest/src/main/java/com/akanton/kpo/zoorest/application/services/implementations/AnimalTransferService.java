package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.events.AnimalMovedEvent;
import com.akanton.kpo.zoorest.application.services.interfaces.IAnimalTransferService;
import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.infrastructure.repositories.AnimalRepository;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnimalTransferService implements IAnimalTransferService {
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EnclosureRepository enclosureRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void transferAnimal(int animalId, int newEnclosureId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        Enclosure oldEnclosure = enclosureRepository.findById(animal.getEnclosureId())
                .orElseThrow(() -> new RuntimeException("Old enclosure not found"));

        Enclosure newEnclosure = enclosureRepository.findById(newEnclosureId)
                .orElseThrow(() -> new RuntimeException("New enclosure not found"));

        if (animal.getAnimalFoodType() != newEnclosure.getAnimalFoodType()) {
            throw new RuntimeException("Animal food type does not match enclosure food type");
        }

        if (animal.getAnimalHabitatType() != newEnclosure.getAnimalHabitatType()) {
            throw new RuntimeException("Animal habitat type does not match enclosure habitat type");
        }

        oldEnclosure.removeAnimal();
        newEnclosure.addAnimal();
        animal.setEnclosureId(newEnclosure.getId());

        animalRepository.save(animal);
        enclosureRepository.save(oldEnclosure);
        enclosureRepository.save(newEnclosure);

        AnimalMovedEvent event = new AnimalMovedEvent(this, animal.getId(), newEnclosure.getId(), animal.getEnclosureId(), new Date());
        eventPublisher.publishEvent(event);
    }
}
