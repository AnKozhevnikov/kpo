package com.anton.kpo.application.services.implementations;


import com.anton.kpo.application.services.interfaces.IDetectDuplicationService;
import com.anton.kpo.infrasructure.repositories.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetectDuplicationService implements IDetectDuplicationService {
    @Autowired
    private AnalysisRepository analysisRepository;

    @Override
    public boolean detectDuplicateFileId(Long fileId) {
        return !analysisRepository.getByFileId(fileId).isEmpty();
    }
}
