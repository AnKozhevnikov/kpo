package com.anton.kpo.infrasructure.repositories;

import com.anton.kpo.domain.AnalysisInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisInfo, Long> {
    List<AnalysisInfo> getByFileId(Long fileId);
}
