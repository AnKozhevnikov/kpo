package com.anton.kpo.application.services.implementations;

import com.anton.kpo.application.services.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class StorageService implements IStorageService {
    @Value("${file.storage.path}")
    private String storagePath;

    @Override
    public void saveFile(InputStream is, String fileName) throws IOException {
        Path dir = Paths.get(storagePath);
        Path target = dir.resolve(fileName);
        Files.copy(is, target);
    }

    @Override
    public InputStream getFile(String fileName) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Файл не найден: " + fileName);
        }
        return Files.newInputStream(filePath, StandardOpenOption.READ);
    }
}
