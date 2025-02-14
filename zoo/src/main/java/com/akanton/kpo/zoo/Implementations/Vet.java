package com.akanton.kpo.zoo.Implementations;

import com.akanton.kpo.zoo.Interfaces.IVet;
import java.util.Scanner;

public class Vet implements IVet
{
	public boolean isHealthy(Animal animal)
	{
		System.out.println("Vet is checking the health of the animal");
		System.out.println("You are the vet. You can check the health of the animal here.");
		Scanner sc = new Scanner(System.in);
		System.out.println("Is the animal healthy? (Y/N)");
		String response = sc.nextLine();
		if (response.equals("Y") || response.equals("y")) {
			System.out.println("The animal is healthy.");
			return true;
		} else {
			System.out.println("The animal is not healthy.");
			return false;
		}
	}
}
