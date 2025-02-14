package com.akanton.kpo.zoo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.akanton.kpo.zoo.Implementations.Animal;
import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Monkey;
import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Rabbit;
import com.akanton.kpo.zoo.Implementations.Animals.Predators.Tiger;
import com.akanton.kpo.zoo.Implementations.Animals.Predators.Wolf;
import com.akanton.kpo.zoo.Interfaces.ICreator;

public class AnimalCreatorTest {
	private ApplicationContext context;
	private ICreator<Animal> animalCreator;
	
	@BeforeEach
	public void setUp() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		animalCreator = (ICreator<Animal>) context.getBean("animalCreator");
	}
	
	@Test
	public void generateMonkey() {
		Animal monkey = animalCreator.create("Monkey", "George", 10, 3);
		assertTrue(monkey.getName().equals("George"));
		assertTrue(monkey.getFood() == 10);
		assertTrue(monkey instanceof Monkey);
		assertTrue(((Monkey)monkey).getFriendship() == 3);
	}
	
	@Test
	public void generateMonkeyWithInvalidFood() {
		Animal monkey = animalCreator.create("Monkey", "George", -10, 3);
		assertTrue(monkey == null);
	}
	
	@Test
	public void generateMonkeyWithInvalidFriendship() {
		Animal monkey = animalCreator.create("Monkey", "George", 10, 13);
		assertTrue(monkey == null);
	}
	
	@Test
	public void generateRabbit() {
		Animal rabbit = animalCreator.create("Rabbit", "Bunny", 5, 5);
		assertTrue(rabbit.getName().equals("Bunny"));
		assertTrue(rabbit.getFood() == 5);
		assertTrue(rabbit instanceof Rabbit);
		assertTrue(((Rabbit) rabbit).getFriendship() == 5);
	}
	
	@Test
	public void generateRabbitWithInvalidFood() {
		Animal rabbit = animalCreator.create("Rabbit", "Bunny", -5, 5);
		assertTrue(rabbit == null);
	}
	
	@Test
	public void generateRabbitWithInvalidFriendship() {
		Animal rabbit = animalCreator.create("Rabbit", "Bunny", 5, 13);
		assertTrue(rabbit == null);
	}
	
	@Test
	public void generateWolf() {
		Animal wolf = animalCreator.create("Wolf", "Grey", 15);
		assertTrue(wolf.getName().equals("Grey"));
		assertTrue(wolf.getFood() == 15);
		assertTrue(wolf instanceof Wolf);
	}
	
	@Test
	public void generateWolfWithInvalidFood() {
		Animal wolf = animalCreator.create("Wolf", "Grey", -15);
		assertTrue(wolf == null);
	}
	
	@Test
	public void generateTiger() {
		Animal tiger = animalCreator.create("Tiger", "Stripes", 20);
		assertTrue(tiger.getName().equals("Stripes"));
		assertTrue(tiger.getFood() == 20);
		assertTrue(tiger instanceof Tiger);
	}
	
	@Test
	public void generateTigerWithInvalidFood() {
		Animal tiger = animalCreator.create("Tiger", "Stripes", -20);
		assertTrue(tiger == null);
	}
	
	@Test
	public void generateInvalidAnimal() {
		Animal animal = animalCreator.create("Invalid", "Invalid", 0);
		assertTrue(animal == null);
	}
}
