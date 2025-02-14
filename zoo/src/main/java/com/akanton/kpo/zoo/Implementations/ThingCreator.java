package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Implementations.Things.Computer;
import com.akanton.kpo.zoo.Implementations.Things.Table;
import com.akanton.kpo.zoo.Interfaces.ICreator;
import com.akanton.kpo.zoo.Interfaces.IIdManager;

public class ThingCreator implements ICreator<Thing> {
	private final IIdManager idManager;

	public ThingCreator(IIdManager idManager) {
		this.idManager = idManager;
	}

	public Thing create(Object... args) {
		String type = (String) args[0];
		type = type.toLowerCase();
		String name = (String) args[1];
		
		switch (type) {
		case "table":
			return new Table(idManager.getNext(), name);
		case "computer":
			return new Computer(idManager.getNext(), name);
		default:
			return null;
		}
	}
}
