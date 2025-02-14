package com.akanton.kpo.zoo;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import com.akanton.kpo.zoo.Interfaces.IVet;
import com.akanton.kpo.zoo.Implementations.Vet;
import com.akanton.kpo.zoo.Interfaces.ICreator;
import com.akanton.kpo.zoo.Interfaces.IIdManager;
import com.akanton.kpo.zoo.Implementations.Animal;
import com.akanton.kpo.zoo.Implementations.IdManager;
import com.akanton.kpo.zoo.Implementations.Thing;
import com.akanton.kpo.zoo.Interfaces.IZooPrinter;
import com.akanton.kpo.zoo.Implementations.ZooPrinter;
import com.akanton.kpo.zoo.Interfaces.IZoo;
import com.akanton.kpo.zoo.Implementations.Zoo;
import com.akanton.kpo.zoo.Implementations.AnimalCreator;
import com.akanton.kpo.zoo.Implementations.ThingCreator;

@Configuration
public class AppConfig {
	@Bean
	public IVet vet() {
		return new Vet();
	}
	
	@Bean
	public IIdManager idManager() {
        return new IdManager();
	}
	
	@Bean
	public IZooPrinter zooPrinter() {
		return new ZooPrinter();
	}
	
	@Bean
	public ICreator<Animal> animalCreator() {
		return new AnimalCreator(idManager());
	}
	
	@Bean
	public ICreator<Thing> thingCreator() {
		return new ThingCreator(idManager());
	}
	
	@Bean
	public IZoo zoo() {
		return new Zoo(vet());
	}
}
