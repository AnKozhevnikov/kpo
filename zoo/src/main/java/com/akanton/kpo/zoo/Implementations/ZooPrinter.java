package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Implementations.Animals.Herbo;
import com.akanton.kpo.zoo.Implementations.Animals.Predator;
import com.akanton.kpo.zoo.Interfaces.IZoo;
import com.akanton.kpo.zoo.Interfaces.IZooPrinter;

public class ZooPrinter implements IZooPrinter {
	public void printInventory(IZoo zoo) {
		printAnimals(zoo);
		printThings(zoo);
	}

	public void printAnimals(IZoo zoo) {
		Iterable<Herbo> herbos = zoo.getHerbos();
		Iterable<Predator> predators = zoo.getPredators();
		for (Herbo herbo : herbos) {
			System.out.println(herbo.getId() + " " + herbo.getType() + " " + herbo.getSpecies() + " " + herbo.getName());
		}
		for (Predator predator : predators) {
            System.out.println(predator.getId() + " " + predator.getType() + " " + predator.getSpecies() + " " + predator.getName());
		}
	}

	public void printThings(IZoo zoo) {
		Iterable<Thing> things = zoo.getThings();
		for (Thing thing : things) {
            System.out.println(thing.getId() + " " + thing.getType() + " " + thing.getName());
		}
	}

	public void printContact(IZoo zoo) {
		Iterable<Herbo> herbos = zoo.getHerbos();
		for (Herbo herbo : herbos) {
			if (herbo.getFriendship() >= 5)
				System.out.println(herbo.getId() + " " + herbo.getType() + " " + herbo.getSpecies() + " " + herbo.getName());
		}
	}

	public void printTotalFood(IZoo zoo) {
		Iterable<Herbo> herbos = zoo.getHerbos();
		Iterable<Predator> predators = zoo.getPredators();
		int totalFood = 0;
		for (Herbo herbo : herbos) {
			totalFood += herbo.getFood();
		}
		for (Predator predator : predators) {
			totalFood += predator.getFood();
		}
		System.out.println("Total food: " + totalFood);
	}
}
