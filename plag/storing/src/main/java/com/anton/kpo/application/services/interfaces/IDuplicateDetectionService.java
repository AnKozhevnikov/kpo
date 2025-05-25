package com.anton.kpo.application.services.interfaces;

public interface IDuplicateDetectionService {
    public boolean detectDuplicateName(String fileName);
    public boolean detectDuplicateHash(String fileHash);
}
