package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Interfaces.IAlive;
import com.akanton.kpo.zoo.Interfaces.IInventory;

public abstract class Animal implements IInventory, IAlive {
	private int id;
	private String name;
	private String type;
	private String species;
	private int food;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFood() {
        return food;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSpecies() {
		return species;
	}
	
	protected Animal(int id, String name, String type, String species, int food) {
		this.id = id;
        this.name = name;
        this.food = food;
        this.type = type;
        this.species = species;
	}
}
