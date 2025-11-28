package com.example.ai_practice_bot.dto;

import java.util.List;

public record FeedbackRequest(
        String userId,
        String question,
        String answer,
        List<Integer> embeddingIds,
        boolean isHelpful
) {}
