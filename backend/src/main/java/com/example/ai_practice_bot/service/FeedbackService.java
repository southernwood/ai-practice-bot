package com.example.ai_practice_bot.service;

import com.example.ai_practice_bot.dto.FeedbackRequest;
import com.example.ai_practice_bot.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public void submitFeedback(FeedbackRequest request) {
        repository.saveFeedback(
                request.userId(),
                request.question(),
                request.answer(),
                request.embeddingIds(),
                request.isHelpful()
        );
    }
}
