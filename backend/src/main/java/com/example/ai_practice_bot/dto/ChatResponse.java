package com.example.ai_practice_bot.dto;

import java.util.List;

public record ChatResponse(
        String answer,
        List<Integer> embeddingIds,
        String question
) {}