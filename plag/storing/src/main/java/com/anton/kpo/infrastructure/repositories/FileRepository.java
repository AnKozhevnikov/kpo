package com.anton.kpo.infrastructure.repositories;

import com.anton.kpo.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findByName(String name);
    List<FileInfo> findByHash(String hash);
}
