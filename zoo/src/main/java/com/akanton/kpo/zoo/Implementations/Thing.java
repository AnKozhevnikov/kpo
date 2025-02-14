package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Interfaces.IInventory;

public abstract class Thing implements IInventory
{
	private int id;
	private String type;
	private String name;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	protected Thing(int id, String type, String name) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
}
