package com.anton.kpo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Main {
    @Value("${file.storage.path}")
    private String storagePath;

    @Bean
    public void initializeStorage() throws Exception {
        Path dir = Paths.get(storagePath);
        Files.createDirectories(dir);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}