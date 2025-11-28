package com.example.ai_practice_bot.entity;

import jakarta.persistence.*; // 或者 javax.persistence 根据你项目依赖
import java.util.Arrays;

@Entity
@Table(name = "embeddings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"content_hash"})})
public class Embedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 可选：标记属于哪个文件
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "chunk_index")
    private Integer chunkIndex;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_hash", unique = true)
    private String contentHash;

    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }

    public Integer getChunkIndex() { return chunkIndex; }
    public void setChunkIndex(Integer chunkIndex) { this.chunkIndex = chunkIndex; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }

    public float[] getEmbedding() { return embedding; }
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }

    @Override
    public String toString() {
        return "Embedding{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", chunkIndex=" + chunkIndex +
                ", contentHash='" + contentHash + '\'' +
                ", content='" + (content != null ? (content.length() > 120 ? content.substring(0,120) + "..." : content) : null) + '\'' +
                ", embedding=" + (embedding != null ? ("len=" + embedding.length) : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Embedding)) return false;
        Embedding that = (Embedding) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(contentHash, that.contentHash);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, contentHash);
    }
}
