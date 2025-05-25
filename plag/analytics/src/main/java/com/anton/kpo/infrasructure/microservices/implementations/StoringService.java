package com.anton.kpo.infrasructure.microservices.implementations;

import com.anton.kpo.infrasructure.microservices.interfaces.IStoringService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class StoringService implements IStoringService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getFileById(Long fileId) {
        Resource resource = restTemplate.getForObject(
                "http://storing-service:8080/files/id/" + fileId,
                Resource.class
        );

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource", e);
        }
    }
}
