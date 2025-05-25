package com.anton.kpo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AnalysisInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long fileId;
    private Long symbols;
    private Long words;
    private Long paragraphs;
    private String picLocation;

    public AnalysisInfo() {
        // Default constructor for JPA
    }

    public AnalysisInfo(Long fileId, Long symbols, Long words, Long paragraphs, String picLocation) {
        this.fileId = fileId;
        this.symbols = symbols;
        this.words = words;
        this.paragraphs = paragraphs;
        this.picLocation = picLocation;
    }

    public Long getId() {
        return id;
    }
    public Long getFileId() {
        return fileId;
    }
    public Long getSymbols() {
        return symbols;
    }
    public Long getWords() {
        return words;
    }
    public Long getParagraphs() {
        return paragraphs;
    }
    public String getPicLocation() {
        return picLocation;
    }
}
