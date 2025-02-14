package com.akanton.kpo.zoo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.akanton.kpo.zoo.Implementations.Zoo;
import com.akanton.kpo.zoo.Interfaces.IVet;
import com.akanton.kpo.zoo.Interfaces.IZoo;

@TestConfiguration
public class TestConfig {

    @MockBean
    private IVet vet;

    @Bean
    @Primary
    public IVet vet() {
        return vet;
    }
    
    @Bean
    public IZoo zoo() {
    	return new Zoo(vet());
    }
}