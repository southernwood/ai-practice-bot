package com.example.ai_practice_bot.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkService {

    /**
     * Split a large text into smaller chunks with optional overlap.
     *
     * @param text      The full text to split
     * @param chunkSize Maximum length of each chunk
     * @param overlap   Number of characters that overlap between consecutive chunks
     * @return List of string chunks
     */
    public List<String> chunkText(String text, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            String chunk = text.substring(start, end);
            chunks.add(chunk);

            start += (chunkSize - overlap);
        }

        return chunks;
    }
}
