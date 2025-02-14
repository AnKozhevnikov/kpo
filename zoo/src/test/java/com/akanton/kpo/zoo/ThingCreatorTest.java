package com.akanton.kpo.zoo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.akanton.kpo.zoo.Implementations.Thing;
import com.akanton.kpo.zoo.Implementations.Things.Computer;
import com.akanton.kpo.zoo.Implementations.Things.Table;
import com.akanton.kpo.zoo.Interfaces.ICreator;

public class ThingCreatorTest {
	private ApplicationContext context;
	private ICreator<Thing> thingCreator;

	@BeforeEach
	public void setUp() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		thingCreator = (ICreator<Thing>) context.getBean("thingCreator");
	}

	@Test
	public void generateTable() {
		Thing thing = thingCreator.create("Table", "IKEAtable");
		assertTrue(thing.getName().equals("IKEAtable"));
		assertTrue(thing instanceof Table);
	}

	@Test
	public void generateComputer() {
		Thing thing = thingCreator.create("Computer", "Dell");
		assertTrue(thing.getName().equals("Dell"));
		assertTrue(thing instanceof Computer);
	}
	
	@Test
	public void generateInvalidThing() {
		Thing thing = thingCreator.create("Invalid", "Invalid");
		assertTrue(thing == null);
	}
}
