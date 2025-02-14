package com.akanton.kpo.zoo.Implementations.Animals;

import com.akanton.kpo.zoo.Implementations.Animal;

public abstract class Predator extends Animal {
	protected Predator(int id, String name, String species, int food) {
		super(id, name, "Predator", species, food);
	}
}
