package com.example.ai_practice_bot.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import com.example.ai_practice_bot.repository.EmbeddingRepository;
import com.example.ai_practice_bot.util.HashUtils;

import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingRepository repository;
    private final ChunkService chunkService;

    public EmbeddingService(EmbeddingModel embeddingModel,
                            EmbeddingRepository repository,
                            ChunkService chunkService) {
        this.embeddingModel = embeddingModel;
        this.repository = repository;
        this.chunkService = chunkService;
    }

    /**
     * Add a large text to the embedding database:
     * 1. Split text into chunks
     * 2. Deduplicate each chunk
     * 3. Generate embeddings
     * 4. Save to database
     *
     * @param text The text to process
     */
    public void addText(String text) {
        // Step 1: Split text into chunks (e.g., 500 chars with 50 overlap)
        List<String> chunks = chunkService.chunkText(text, 500, 50);

        for (String chunk : chunks) {
            // Step 2: Compute SHA-256 hash for deduplication
            String hash = HashUtils.sha256(chunk);

            // Step 3: Skip if chunk already exists
            if (repository.existsByContentHash(hash)) {
                System.out.println("Duplicate chunk detected, skipping hash=" + hash);
                continue;
            }

            // Step 4: Generate embedding using your EmbeddingModel
            float[] vector = embeddingModel.embed(chunk);

            // Step 5: Save chunk and embedding to database
            repository.saveEmbedding(chunk, vector);
        }
    }

    public float[] getEmbeddingForText(String text) {
        return embeddingModel.embed(text);
    }
}