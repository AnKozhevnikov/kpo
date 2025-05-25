package com.anton.kpo.presentation;

import com.anton.kpo.application.services.interfaces.IDuplicateDetectionService;
import com.anton.kpo.application.services.interfaces.IHasherService;
import com.anton.kpo.application.services.interfaces.IStorageService;
import com.anton.kpo.domain.FileInfo;
import com.anton.kpo.infrastructure.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
public class SaveLoadController {
    @Autowired
    private IDuplicateDetectionService duplicateDetectionService;
    @Autowired
    private IHasherService hasherService;
    @Autowired
    private IStorageService storageService;
    @Autowired
    private FileRepository fileRepository;

    @Value("${file.storage.path}")
    private String storagePath;

    @GetMapping("/files/filename/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(fileName).normalize();

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/files/id/{id}")
    public ResponseEntity<Resource> getFileById(@PathVariable Long id) {
        try {
            Optional<FileInfo> file = fileRepository.findById(id);
            if (file.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            String fileName = file.get().getName();
            return getFile(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping(value = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadFile(@RequestPart("file") MultipartFile file) throws IOException, NoSuchAlgorithmException {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String hash = hasherService.hashFile(file.getInputStream());

            if (duplicateDetectionService.detectDuplicateName(fileName) ||
                duplicateDetectionService.detectDuplicateHash(hash)) {
                return ResponseEntity.status(409).build();
            }

            storageService.saveFile(file.getInputStream(), fileName);

            FileInfo fileInfo = new FileInfo(fileName, hash);
            fileRepository.save(fileInfo);

            System.out.println("File uploaded: " + fileName + " with hash: " + hash);

            return ResponseEntity.ok(fileInfo.getId());
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
