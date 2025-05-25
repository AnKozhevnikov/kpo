package com.anton.kpo.application.services.implementations;

import com.anton.kpo.application.services.interfaces.IStatsService;
import org.springframework.stereotype.Service;

@Service
public class StatsService implements IStatsService {
    @Override
    public Long getSymbolsCount(String text) {
        return (long) text.length();
    }

    @Override
    public Long getWordsCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0L;
        }
        String[] words = text.trim().split("\\s+");
        return (long) words.length;
    }

    @Override
    public Long getParagraphsCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0L;
        }
        String[] paragraphs = text.split("\\n+");
        return (long) paragraphs.length;
    }
}
