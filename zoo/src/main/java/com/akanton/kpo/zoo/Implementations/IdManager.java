package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Interfaces.IIdManager;

public class IdManager implements IIdManager {
	private int nextId = 0;

	public int getNext() {
		return nextId++;
	}
}
