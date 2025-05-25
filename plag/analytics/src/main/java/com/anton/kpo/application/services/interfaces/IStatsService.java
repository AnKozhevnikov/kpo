package com.anton.kpo.application.services.interfaces;

public interface IStatsService {
    Long getSymbolsCount(String text);
    Long getWordsCount(String text);
    Long getParagraphsCount(String text);
}
