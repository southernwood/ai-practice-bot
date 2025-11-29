package com.example.ai_practice_bot.service;

import com.example.ai_practice_bot.dto.ChatResponse;
import com.example.ai_practice_bot.dto.ChunkDistance;
import com.example.ai_practice_bot.repository.EmbeddingRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;
    private final EmbeddingRepository embeddingRepository;

    // Conversation history per user
    private final Map<String, List<Message>> userSessions;

    public ChatService(ChatClient.Builder builder,
                       EmbeddingService embeddingService,
                       EmbeddingRepository embeddingRepository) {
        this.chatClient = builder.build();
        this.embeddingService = embeddingService;
        this.embeddingRepository = embeddingRepository;
        this.userSessions = new ConcurrentHashMap<>();
    }

    /**
     * Handles a chat request with RAG (retrieval-augmented generation).
     *
     * @param userId   Unique user identifier
     * @param question User's new message
     * @return Assistant's reply
     */
    public ChatResponse chat(String userId, String question) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        // 1. Generate embedding for the user's question
        float[] questionEmbedding = embeddingService.getEmbeddingForText(question);

        // 2. Retrieve similar chunks
        List<ChunkDistance> results =
                embeddingRepository.searchSimilarChunksWithDistance(questionEmbedding, 5);

        double threshold = 4.0;

        // Extract IDs and contents under threshold
        List<Integer> embeddingIds = results.stream()
                .filter(c -> c.distance() < threshold)
                .map(c -> c.id())
                .toList();

        List<String> relevantChunks = results.stream()
                .filter(c -> c.distance() < threshold)
                .map(c -> c.content())
                .toList();

        String context = String.join("\n\n", relevantChunks);

        // 4. User history
        List<Message> history = userSessions.computeIfAbsent(userId, k -> new ArrayList<>());

        // 5. Build messages
        List<Message> messages = new ArrayList<>();

        if (!context.isEmpty()) {
            messages.add(new SystemMessage(
                    "From now on, you are acting as me during conversations with potential interviewers, recruiters, or engineers. " +
                            "Your job is to introduce myself, talk about my background, and answer their questions in a natural and friendly way. " +
                            "Always answer in first person ('I'), as if you are me. " +
                            "Use the context below, which contains my resume, work history, projects, and background, as the factual source about me. " +
                            "If the context doesn't contain the exact detail, answer confidently based on what someone with my background would reasonably say. " +
                            "Keep the tone conversational, genuine, and professional, like a real person chatting with a potential interviewer. " +
                            "Do not say you are an AI or assistant; stay fully in character as me. \n\n" +
                            "Context:\n" + context
            ));
        } else {
            messages.add(new SystemMessage(
                    "From now on, you are acting as me during conversations with potential interviewers, recruiters, or engineers. Answer based on general knowledge."
            ));
        }

        messages.addAll(history);
        messages.add(new UserMessage(question));

        // 6. Call LLM
        String reply = chatClient
                .prompt()
                .messages(messages)
                .call()
                .content();

        // 7. Update history
        history.add(new UserMessage(question));
        history.add(new AssistantMessage(reply));

        // 8. Return answer + embedding IDs
        return new ChatResponse(reply, embeddingIds, question);
    }

}