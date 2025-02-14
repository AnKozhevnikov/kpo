package com.akanton.kpo.zoo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.akanton.kpo.zoo.Implementations.Animal;
import com.akanton.kpo.zoo.Implementations.Thing;
import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Monkey;
import com.akanton.kpo.zoo.Implementations.Animals.Herbos.Rabbit;
import com.akanton.kpo.zoo.Implementations.Animals.Predators.Tiger;
import com.akanton.kpo.zoo.Implementations.Things.Computer;
import com.akanton.kpo.zoo.Implementations.Things.Table;
import com.akanton.kpo.zoo.Interfaces.IVet;
import com.akanton.kpo.zoo.Interfaces.IZoo;
import com.akanton.kpo.zoo.Interfaces.IZooPrinter;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
public class ZooPrinterTest {
	@Autowired
	private IZoo zoo;
	
	@Autowired
	private IVet vet;
	
	private IZooPrinter zooPrinter;
	
	private ArrayList<Animal> herbosFriendly;
	private ArrayList<Animal> herbosUnfriendly;
	private ArrayList<Animal> predators;
	private ArrayList<Thing> things;
	
	@BeforeEach
	public void setUp() {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		zooPrinter = (IZooPrinter) context.getBean("zooPrinter");
		
		herbosFriendly = new ArrayList<Animal>();
		herbosFriendly.add(new Monkey(1, "George", 10, 7));
		herbosFriendly.add(new Monkey(2, "Mickey", 11, 8));
		herbosFriendly.add(new Monkey(3, "Donald", 12, 9));
		herbosFriendly.add(new Rabbit(4, "Daisy", 13, 10));
		
		herbosUnfriendly = new ArrayList<Animal>();
		herbosUnfriendly.add(new Monkey(5, "Goofy", 10, 3));
		herbosUnfriendly.add(new Monkey(6, "Pluto", 11, 2));
		herbosUnfriendly.add(new Monkey(7, "Minnie", 12, 1));
		herbosUnfriendly.add(new Rabbit(8, "Chip", 13, 0));
		
		predators = new ArrayList<Animal>();
		predators.add(new Tiger(9, "Dale", 10));
		predators.add(new Tiger(10, "Clarice", 11));
		predators.add(new Tiger(11, "Gadget", 12));
		predators.add(new Tiger(12, "Zipper", 13));
		
		when(vet.isHealthy(any())).thenReturn(true);
		
		zoo.addAnimal(herbosFriendly.get(0));
		zoo.addAnimal(herbosFriendly.get(1));
		zoo.addAnimal(herbosFriendly.get(2));
		zoo.addAnimal(herbosFriendly.get(3));
		zoo.addAnimal(herbosUnfriendly.get(0));
		zoo.addAnimal(herbosUnfriendly.get(1));
		zoo.addAnimal(herbosUnfriendly.get(2));
		zoo.addAnimal(herbosUnfriendly.get(3));
		zoo.addAnimal(predators.get(0));
		zoo.addAnimal(predators.get(1));
		zoo.addAnimal(predators.get(2));
		zoo.addAnimal(predators.get(3));
		
		zoo.removeAnimal(1);
		zoo.removeAnimal(5);
		zoo.removeAnimal(9);
		zoo.removeAnimal(100);
		
	    things = new ArrayList<Thing>();
	    things.add(new Table(13, "MyTable"));
	    things.add(new Table(14, "YourTable"));
	    things.add(new Computer(16, "MyComputer"));
	    
	    zoo.addThing(things.get(0));
	    zoo.addThing(things.get(1));
	    zoo.addThing(things.get(2));
	    
	    zoo.removeThing(14);
	    zoo.removeThing(100);
	}
	
	@Test
	public void printZoo() {
		zooPrinter.printAnimals(zoo);
		zooPrinter.printThings(zoo);
		zooPrinter.printInventory(zoo);
		zooPrinter.printTotalFood(zoo);
		zooPrinter.printContact(zoo);
	}
}
