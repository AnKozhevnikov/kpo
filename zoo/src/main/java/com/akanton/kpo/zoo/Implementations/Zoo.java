package com.akanton.kpo.zoo.Implementations;

import java.util.HashMap;
import java.util.HashSet;

import com.akanton.kpo.zoo.Implementations.Animals.Herbo;
import com.akanton.kpo.zoo.Implementations.Animals.Predator;
import com.akanton.kpo.zoo.Interfaces.IVet;
import com.akanton.kpo.zoo.Interfaces.IZoo;

public class Zoo implements IZoo {
	private IVet vet;
	private HashMap<Integer, Herbo> herbos;
	private HashMap<Integer, Predator> predators;
	private HashMap<Integer, Thing> things;
	
	public Zoo(IVet vet) {
		this.vet = vet;
		herbos = new HashMap<Integer, Herbo>();
		predators = new HashMap<Integer, Predator>();
		things = new HashMap<Integer, Thing>();
	}

	public void addAnimal(Animal animal) {
		boolean isHealthy = vet.isHealthy(animal);
		if (isHealthy) {
			if (animal instanceof Herbo) {
				herbos.put(animal.getId(), (Herbo) animal);
			} else if (animal instanceof Predator) {
				predators.put(animal.getId(), (Predator) animal);
			}
		}
	}

	public void removeAnimal(int id) {
		if (herbos.containsKey(id)) {
			herbos.remove(id);
		} else if (predators.containsKey(id)) {
			predators.remove(id);
		}
	}

	public void addThing(Thing thing) {
		things.put(thing.getId(), thing);
	}

	public void removeThing(int id) {
		things.remove(id);
	}
	
	public Iterable<Animal> getAnimals() {
		HashSet<Animal> animals = new HashSet<Animal>();
		animals.addAll(herbos.values());
		animals.addAll(predators.values());
		return animals;
	}
	
	public Iterable<Thing> getThings() {
		return things.values();
	}
	
	public Iterable<Herbo> getHerbos() {
		return herbos.values();
	}
	
	public Iterable<Predator> getPredators() {
		return predators.values();
	}
}
