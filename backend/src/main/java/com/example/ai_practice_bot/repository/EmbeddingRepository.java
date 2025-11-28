package com.example.ai_practice_bot.repository;

import com.example.ai_practice_bot.dto.ChunkDistance;
import com.example.ai_practice_bot.util.HashUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmbeddingRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmbeddingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByContentHash(String hash) {
        String sql = "SELECT COUNT(*) FROM embeddings WHERE content_hash = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, hash);
        return count != null && count > 0;
    }

    public void saveEmbedding(String content, float[] embedding) {
        String hash = HashUtils.sha256(content);

        if (existsByContentHash(hash)) {
            System.out.println("Duplicate content detected, skipping.");
            return;
        }

        String sql = "INSERT INTO embeddings (content, content_hash, embedding) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, content, hash, embedding);

        System.out.println("Inserted new chunk with hash=" + hash);
    }

    /**
     * Search top similar chunks with distances.
     * Returns a list of Pair<content, distance>.
     */
    public List<ChunkDistance> searchSimilarChunksWithDistance(float[] embedding, int limit) {
        String sql = """
            SELECT id, content, embedding <-> ?::vector AS distance
            FROM embeddings
            ORDER BY distance
            LIMIT ?
        """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, embedding);
                    ps.setInt(2, limit);
                },
                (rs, rowNum) -> new ChunkDistance(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getDouble("distance")
                )
        );
    }


}
