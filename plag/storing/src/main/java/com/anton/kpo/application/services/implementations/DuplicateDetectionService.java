package com.anton.kpo.application.services.implementations;


import com.anton.kpo.application.services.interfaces.IDuplicateDetectionService;
import com.anton.kpo.infrastructure.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DuplicateDetectionService implements IDuplicateDetectionService {

    @Autowired
    FileRepository storedFileRepository;

    @Override
    public boolean detectDuplicateName(String fileName) {
        return !storedFileRepository.findByName(fileName).isEmpty();
    }

    @Override
    public boolean detectDuplicateHash(String fileHash) {
        return !storedFileRepository.findByHash(fileHash).isEmpty();
    }
}