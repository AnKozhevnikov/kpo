package com.akanton.kpo.zoorest.application.services.implementations;

import com.akanton.kpo.zoorest.application.services.interfaces.IEnclosureService;
import com.akanton.kpo.zoorest.domain.Enclosure;
import com.akanton.kpo.zoorest.infrastructure.repositories.EnclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnclosureService implements IEnclosureService {
    @Autowired
    private EnclosureRepository enclosureRepository;

    public void cleanEnclosure(int enclosureId) {
        Enclosure enclosure = enclosureRepository.findById(enclosureId)
                .orElseThrow(() -> new RuntimeException("Enclosure not found"));
        enclosure.clean();
        enclosureRepository.save(enclosure);
    }
}
