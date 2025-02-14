package com.akanton.kpo.zoo;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.akanton.kpo.zoo.Implementations.Animal;
import com.akanton.kpo.zoo.Implementations.Thing;
import com.akanton.kpo.zoo.Interfaces.ICreator;
import com.akanton.kpo.zoo.Interfaces.IZoo;
import com.akanton.kpo.zoo.Interfaces.IZooPrinter;

public class App {
    public static void main(String[] args) {
    	ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    	IZoo zoo = (IZoo) context.getBean("zoo");
    	IZooPrinter zooPrinter = (IZooPrinter) context.getBean("zooPrinter");
    	ICreator<Animal> animalCreator = (ICreator<Animal>) context.getBean("animalCreator");
    	ICreator<Thing> thingCreator = (ICreator<Thing>) context.getBean("thingCreator");
    	
    	System.out.println("1. Add animal");
		System.out.println("2. Remove animal");
		System.out.println("3. Add thing");
		System.out.println("4. Remove thing");
		System.out.println("5. Print animals");
		System.out.println("6. Print things");
		System.out.println("7. Print contact animals");
		System.out.println("8. Print total daily food");
		System.out.println("9. Exit");
    	Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Enter your choice: ");
			String strChoice = scanner.nextLine();
			int choice = 0;
			try {
				choice = Integer.parseInt(strChoice);
			} catch (NumberFormatException e) {
				System.out.println("Invalid choice.");
				continue;
			}
			
			switch (choice) {
			case 1:
				System.out.println("Enter the type of the animal, 1 if herbo, 2 if predator: ");
				int type = scanner.nextInt();
				switch (type) {
				case 1:
					System.out.println("Enter the species of the animal: ");
					String species = scanner.next();
					System.out.println("Enter the name of the animal: ");
					String name = scanner.next();
					System.out.println("Enter the food of the animal: ");
					int food = scanner.nextInt();
					if (food < 0) {
						System.out.println("Invalid food.");
						break;
					}
					System.out.println("Enter the friendship level of the animal from 0 to 10: ");
					int friendship = scanner.nextInt();
					if (friendship < 0 || friendship > 10) {
						System.out.println("Invalid friendship level.");
						break;
					}
					Animal animal = animalCreator.create(species, name, food, friendship);
					if (animal != null) {
                        zoo.addAnimal(animal);
					} else {
						System.out.println("Invalid species.");
					}
					break;
				case 2:
					System.out.println("Enter the species of the animal: ");
					species = scanner.next();
					System.out.println("Enter the name of the animal: ");
					name = scanner.next();
					System.out.println("Enter the food of the animal: ");
					food = scanner.nextInt();
					if (food < 0) {
						System.out.println("Invalid food.");
						break;
					}
					animal = animalCreator.create(species, name, food);
					break;
				default:
					System.out.println("Invalid animal.");
					break;
				}
				break;
			case 2:
				System.out.println("Enter the id of the animal: ");
				int id = scanner.nextInt();
				zoo.removeAnimal(id);
				break;
			case 3:
				System.out.println("Enter the type of the thing: ");
				String thingType = scanner.next();
				System.out.println("Enter the name of the thing: ");
				String name = scanner.next();
				Thing thing = thingCreator.create(thingType, name);
				if (thing != null) {
					zoo.addThing(thing);
				}
				break;
			case 4:
				System.out.println("Enter the id of the thing: ");
				id = scanner.nextInt();
				zoo.removeThing(id);
				break;
			case 5:
				zooPrinter.printAnimals(zoo);
				break;
			case 6:
				zooPrinter.printThings(zoo);
				break;
			case 7:
				zooPrinter.printContact(zoo);
				break;
			case 8:
				zooPrinter.printTotalFood(zoo);
				break;
			case 9:
				scanner.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid choice.");
				break;
			}
		}
    }
}
