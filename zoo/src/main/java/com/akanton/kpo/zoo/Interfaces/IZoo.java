package com.akanton.kpo.zoo.Interfaces;

import com.akanton.kpo.zoo.Implementations.Animal;
import com.akanton.kpo.zoo.Implementations.Thing;
import com.akanton.kpo.zoo.Implementations.Animals.Herbo;
import com.akanton.kpo.zoo.Implementations.Animals.Predator;

public interface IZoo
{
	public void addAnimal(Animal animal);
	public void removeAnimal(int id);
	public void addThing(Thing thing);
	public void removeThing(int id);
	
	public Iterable<Animal> getAnimals();
	public Iterable<Thing> getThings();
	public Iterable<Herbo> getHerbos();
	public Iterable<Predator> getPredators();
}
