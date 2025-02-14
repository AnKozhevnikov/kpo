package com.akanton.kpo.zoo.Implementations.Animals;

import com.akanton.kpo.zoo.Implementations.Animal;

public abstract class Herbo extends Animal {
	private int friendship;
	
	public int getFriendship() {
        return friendship;
	}
	
	protected Herbo(int id, String name, int food, String species, int friendship) {
		super(id, name, "Herbo", species, food);
		this.friendship = friendship;
	}
}
