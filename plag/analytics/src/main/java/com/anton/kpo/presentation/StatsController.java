package com.anton.kpo.presentation;

import com.anton.kpo.application.services.interfaces.IDetectDuplicationService;
import com.anton.kpo.application.services.interfaces.IStatsService;
import com.anton.kpo.application.services.interfaces.IStorageService;
import com.anton.kpo.domain.AnalysisInfo;
import com.anton.kpo.infrasructure.externalapi.interfaces.IWordCloudApi;
import com.anton.kpo.infrasructure.microservices.interfaces.IStoringService;
import com.anton.kpo.infrasructure.repositories.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
public class StatsController {
    @Autowired
    private IStatsService statsService;

    @Autowired
    private IDetectDuplicationService detectDuplicationService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IWordCloudApi wordCloudApi;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private IStoringService storingService;

    @Value("${file.storage.path}")
    private String storagePath;

    private void generateInfo(Long fileId) throws Exception {
        List<AnalysisInfo> analysisInfos = analysisRepository.getByFileId(fileId);
        if (analysisInfos.isEmpty()) {
            System.out.println("Generating analysis info for file ID: " + fileId);
            String file = storingService.getFileById(fileId);
            System.out.println("File retrieved: " + file);
            if (file == null) {
                throw new Exception("File not found");
            }

            Long symbolsCount = statsService.getSymbolsCount(file);
            Long wordsCount = statsService.getWordsCount(file);
            Long paragraphsCount = statsService.getParagraphsCount(file);

            String fileName = UUID.randomUUID().toString() + ".png";

            byte[] wordCloudImage = wordCloudApi.generateWordCloud(file);

            if (wordCloudImage != null) {
                InputStream inputStream = new ByteArrayInputStream(wordCloudImage);
                storageService.saveFile(inputStream, fileName);
            } else {
                throw new Exception("Can't generate word cloud image");
            }

            AnalysisInfo analysisInfo = new AnalysisInfo(fileId, symbolsCount, wordsCount, paragraphsCount, fileName);
            analysisRepository.save(analysisInfo);
        }
    }

    @GetMapping("/stats/info")
    public ResponseEntity<String> getStatsInfo(@RequestParam Long fileId) throws Exception {
        System.out.println("Fetching stats info for file ID: " + fileId);
        List<AnalysisInfo> analysisInfos = analysisRepository.getByFileId(fileId);
        AnalysisInfo analysisInfo = null;
        if (analysisInfos.isEmpty()) {
            generateInfo(fileId);
            analysisInfos = analysisRepository.getByFileId(fileId);
            analysisInfo = analysisInfos.get(0);
        }
        else
        {
            analysisInfo = analysisInfos.get(0);
        }

        return ResponseEntity.ok(
                "Symbols Count: " + analysisInfo.getSymbols() + "\n" +
                "Words Count: " + analysisInfo.getWords() + "\n" +
                "Paragraphs Count: " + analysisInfo.getParagraphs());
    }

    @GetMapping("/stats/wordcloud")
    public ResponseEntity<byte[]> getWordCloud(@RequestParam Long fileId) throws Exception {
        List<AnalysisInfo> analysisInfos = analysisRepository.getByFileId(fileId);
        if (analysisInfos.isEmpty()) {
            generateInfo(fileId);
            analysisInfos = analysisRepository.getByFileId(fileId);
        }

        AnalysisInfo analysisInfo = analysisInfos.get(0);
        String fileName = analysisInfo.getPicLocation();

        InputStream wordCloudImage = storageService.getFile(fileName);

        if (wordCloudImage == null) {
            throw new Exception("Word cloud image not found");
        }

        byte[] imageBytes = wordCloudImage.readAllBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(imageBytes);
    }
}
