package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Monkey;
import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Rabbit;
import com.akanton.kpo.zoo.Implementations.Animals.Predators.Tiger;
import com.akanton.kpo.zoo.Implementations.Animals.Predators.Wolf;
import com.akanton.kpo.zoo.Interfaces.ICreator;
import com.akanton.kpo.zoo.Interfaces.IIdManager;

public class AnimalCreator implements ICreator<Animal> {
	private final IIdManager idManager;
	
	public AnimalCreator(IIdManager idManager) {
		this.idManager = idManager;
	}
	
	public Animal create(Object... args) {
		String species = (String) args[0];
		species = species.toLowerCase();
		String name = (String) args[1];
		int food = (Integer) args[2];
		if (food < 0) {
			return null;
		}
		
		switch (species) {
		case "monkey":
			Integer friendship = (Integer) args[3];
			if (friendship < 0 || friendship > 10) {
                return null;
			}
			return new Monkey(idManager.getNext(), name, food, friendship);
		case "rabbit":
			friendship = (Integer) args[3];
			if (friendship < 0 || friendship > 10) {
                return null;
			}
			return new Rabbit(idManager.getNext(), name, food, friendship);
		case "wolf":
			return new Wolf(idManager.getNext(), name, food);
		case "tiger":
			return new Tiger(idManager.getNext(), name, food);
		default:
			return null;
		}
	}
}
