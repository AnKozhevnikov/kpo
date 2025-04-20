package com.akanton.kpo.zoorest.application.services.interfaces;

import com.akanton.kpo.zoorest.domain.Animal;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.domain.Food;

public interface IEntityService {
    public void addAnimal(Animal animal);
    public void removeAnimal(int animalId);
    public void addFoodType(Food food);
    public void removeFoodType(int foodId);
    public void addEnclosure(Enclosure enclosure);
    public void removeEnclosure(int enclosureId);
}
