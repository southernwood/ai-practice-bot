package com.example.ai_practice_bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepository {

    private final JdbcTemplate jdbcTemplate;

    public FeedbackRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveFeedback(String userId, String question, String answer,
                             List<Integer> embeddingIds, boolean isHelpful) {

        String sql = "INSERT INTO feedback (user_id, question, answer, embedding_ids, is_helpful) " +
                "VALUES (?, ?, ?, ?, ?)";

        Integer[] array = embeddingIds == null ? new Integer[]{} : embeddingIds.toArray(new Integer[0]);

        jdbcTemplate.update(sql, userId, question, answer, array, isHelpful);
    }
}
