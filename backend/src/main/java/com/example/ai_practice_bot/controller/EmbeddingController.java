package com.example.ai_practice_bot.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.ai_practice_bot.service.EmbeddingService;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    /**
     * Add embedding for plain text via JSON
     */
    @PostMapping
    public String createEmbedding(@RequestBody EmbeddingRequest request) {
        embeddingService.addText(request.getText());
        return "Saved embedding for text: " + request.getText();
    }

    /**
     * Upload a text file and process its contents into embeddings
     */
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) return "Empty file";

        // Read file content as UTF-8 string
        String text = new String(file.getBytes(), StandardCharsets.UTF_8);

        // Pass the text to your embedding service (it will chunk, deduplicate, embed)
        embeddingService.addText(text);

        return "File processed successfully!";
    }

    /**
     * DTO for JSON text request
     */
    public static class EmbeddingRequest {
        private String text;
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
