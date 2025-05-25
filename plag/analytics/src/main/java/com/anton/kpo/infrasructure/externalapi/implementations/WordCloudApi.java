package com.anton.kpo.infrasructure.externalapi.implementations;

import com.anton.kpo.infrasructure.externalapi.interfaces.IWordCloudApi;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class WordCloudApi implements IWordCloudApi {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public byte[] generateWordCloud(String text) throws Exception {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = "https://quickchart.io/wordcloud?format=png&width=500&height=500&text=" + encodedText;

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.IMAGE_PNG));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch image: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error generating word cloud", e);
        }
    }
}
