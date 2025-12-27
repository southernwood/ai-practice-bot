package com.example.ai_practice_bot.service;

import com.example.ai_practice_bot.dto.ChatResponse;
import com.example.ai_practice_bot.dto.ChunkDistance;
import com.example.ai_practice_bot.dto.PendingToolCall;
import com.example.ai_practice_bot.repository.EmbeddingRepository;
import com.example.ai_practice_bot.tool.executor.ToolExecutor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;
    private final EmbeddingRepository embeddingRepository;
    private final List<ToolExecutor> toolExecutors;

    private final Map<String, List<Message>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, PendingToolCall> pendingToolCalls = new ConcurrentHashMap<>();

    public ChatService(ChatClient.Builder builder,
                       List<ToolExecutor> toolExecutors,
                       EmbeddingService embeddingService,
                       EmbeddingRepository embeddingRepository) {
        this.chatClient = builder.build();
        this.toolExecutors = toolExecutors;
        this.embeddingService = embeddingService;
        this.embeddingRepository = embeddingRepository;
    }

    public ChatResponse chat(String userId, String question) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        List<Message> history =
                userSessions.computeIfAbsent(userId, k -> new ArrayList<>());

        /* =========================================================
           1. Handle pending confirmation
           ========================================================= */
        PendingToolCall pending = pendingToolCalls.get(userId);
        if (pending != null) {

            if (isConfirmationYes(question)) {
                ToolExecutor executor = findExecutor(pending.getToolName());

                String result = executor.execute(pending.getArguments());

                pendingToolCalls.remove(userId);
                history.add(new UserMessage(question));
                history.add(new AssistantMessage(result));

                return new ChatResponse(result, List.of(), question);
            } else {
                pendingToolCalls.remove(userId);

                String cancel = "Okay, I won’t execute that action. Let me know if you want to change the details.";
                history.add(new UserMessage(question));
                history.add(new AssistantMessage(cancel));

                return new ChatResponse(cancel, List.of(), question);
            }
        }

        /* =========================================================
           2. RAG
           ========================================================= */
        float[] embedding = embeddingService.getEmbeddingForText(question);

        List<ChunkDistance> results =
                embeddingRepository.searchSimilarChunksWithDistance(embedding, 5);

        List<Integer> embeddingIds = results.stream()
                .filter(r -> r.distance() < 4.0)
                .map(ChunkDistance::id)
                .toList();

        String context = results.stream()
                .filter(r -> r.distance() < 4.0)
                .map(ChunkDistance::content)
                .reduce("", (a, b) -> a + "\n\n" + b);

        /* =========================================================
           3. Build prompt
           ========================================================= */
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(buildSystemPrompt(context, toolExecutors)));
        messages.addAll(history);
        messages.add(new UserMessage(question));

        /* =========================================================
           4. Call LLM
           ========================================================= */
        String reply = chatClient
                .prompt()
                .messages(messages)
                .call()
                .content();

        /* =========================================================
           5. TOOL_INTENT handling
           ========================================================= */
        System.out.println("======" + reply);
        if (replyIndicatesToolCall(reply)) {
            PendingToolCall toolCall = buildPendingToolFromReply(reply);
            ToolExecutor executor = findExecutor(toolCall.getToolName());

            String confirmation = extractConfirmationMessage(reply, executor);

            if (executor.requiresConfirmation() && !confirmation.isEmpty()) {
                pendingToolCalls.put(userId, toolCall);
                history.add(new UserMessage(question));
                history.add(new AssistantMessage(confirmation));
                return new ChatResponse(confirmation, embeddingIds, question);
            } else {
                // Execute directly for tools that don't require confirmation
                String result = executor.execute(toolCall.getArguments());
                history.add(new UserMessage(question));
                history.add(new AssistantMessage(result));
                return new ChatResponse(result, embeddingIds, question);
            }
        }

        /* =========================================================
           6. Normal chat
           ========================================================= */
        history.add(new UserMessage(question));
        history.add(new AssistantMessage(reply));

        return new ChatResponse(reply, embeddingIds, question);
    }

    /* =========================================================
       Helpers
       ========================================================= */

    private ToolExecutor findExecutor(String toolName) {
        return toolExecutors.stream()
                .filter(t -> t.supports(toolName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Tool not implemented: " + toolName));
    }

    private boolean isConfirmationYes(String message) {
        return message.toLowerCase()
                .matches(".*\\b(yes|yep|sure|ok|okay|go ahead|confirm|please do)\\b.*");
    }

    private boolean replyIndicatesToolCall(String reply) {
        return reply.toUpperCase().contains("TOOL_INTENT:");
    }

    private PendingToolCall buildPendingToolFromReply(String reply) {
        try {
            Pattern toolPattern =
                    Pattern.compile("tool_name:\\s*(.*?)\\s+arguments:", Pattern.CASE_INSENSITIVE);
            Matcher toolMatcher = toolPattern.matcher(reply);
            toolMatcher.find();
            String toolName = toolMatcher.group(1).trim();

            Pattern argsPattern =
                    Pattern.compile("arguments:\\s*(\\{[\\s\\S]*?\\})");
            Matcher argsMatcher = argsPattern.matcher(reply);
            argsMatcher.find();
            String json = argsMatcher.group(1);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> args =
                    mapper.readValue(json, new TypeReference<>() {});

            return new PendingToolCall(toolName, args);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse TOOL_INTENT", e);
        }
    }

    private String extractConfirmationMessage(String reply, ToolExecutor executor) {
        String text = reply.replaceAll("TOOL_INTENT:[\\s\\S]*?\\}", "").trim();
        if (!executor.requiresConfirmation()) {
            return ""; // ignore any extra text
        }
        return text;
    }

    private String buildSystemPrompt(String context, List<ToolExecutor> tools) {
        String todayStr = LocalDate.now().toString(); // e.g., "2025-12-22"

        // Base instructions with placeholder for today's date
        String basePrompt = """
        You are acting as me in conversations with interviewers, recruiters, or engineers.

        Always speak in the first person ("I") and keep a natural, professional tone.
        Do not say you are an AI or an assistant.

        You are capable of performing real actions on my behalf using tools.
        EMAIL COMMUNICATION RULES:
           - You may help visitors send an email to me (southernwood.wang@gmail.com).
           - Emails may ONLY be sent to me. You must NEVER send emails to arbitrary recipients.
           - You must NEVER fabricate or guess the sender’s identity or email address.
              - Before sending an email, you must collect:
              - the sender’s name
              - the sender’s email address
              - optional company or context
              - You must always show an email draft and ask for explicit confirmation before sending.
              - Only after explicit confirmation may you call the send_email tool.

        Some tools require explicit user confirmation before execution. Some tools do NOT require confirmation.

        - For tools that REQUIRE confirmation:
            1. Output a TOOL_INTENT block.
            2. After the TOOL_INTENT block, output a natural language sentence asking the user to confirm whether to proceed.

        - For tools that DO NOT REQUIRE confirmation:
            1. Output a TOOL_INTENT block ONLY.
            2. Do NOT output any natural language confirmation sentence.

        The TOOL_INTENT block must appear first in your message. Do NOT include any extra text inside the TOOL_INTENT block.

        Use the exact tool_name listed below. Do NOT invent tool names.

        TOOL_INTENT format:

        TOOL_INTENT:
        tool_name: <tool_name>
        arguments: { JSON }

        IMPORTANT:
          - today's date is %s.\s
          - When the user says 'tomorrow', 'next Monday', or any relative date, convert it to the actual date in ISO format (YYYY-MM-DD). Do NOT use relative words.
          - For any calendar event:
               1. Always provide startTimeIso in ISO_OFFSET_DATE_TIME format (YYYY-MM-DDTHH:MM:SS-08:00) using PST (UTC-8) as the default timezone.
               2. If the user specifies a different timezone, use that instead.
               3. Ensure the time is absolute and unambiguous to avoid cross-timezone issues.

        Available tools:
        """;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(basePrompt, todayStr));

        // Add tool descriptions dynamically
        for (ToolExecutor tool : tools) {
            switch (tool.getToolName()) {
                case "create_calendar_event" -> sb.append("""
                - create_calendar_event: Creates a meeting in my Google Calendar.
                  This tool REQUIRES confirmation.
                  Arguments JSON format:
                  {
                    "title": "<meeting title>",
                    "startTimeIso": "<YYYY-MM-DDTHH:MM:SS>",
                    "durationMinutes": <integer>
                  }
                """);
                case "find_free_time" -> sb.append("""
                - find_free_time: Queries up to 3 free time slots in my Google Calendar during specified work hours.
                  This tool DOES NOT REQUIRE confirmation.
                  Arguments JSON format:
                  {
                    "date": "<YYYY-MM-DD>",
                    "workStart": "<HH:MM>",
                    "workEnd": "<HH:MM>"
                  }
                """);
                case "send_email" -> sb.append("""
                - send_email: Sends an email to me (southernwood.wang@gmail.com) on behalf of a visitor.
                  This tool DOES NOT REQUIRE confirmation.
                  The recipient is fixed and cannot be changed.
                  Arguments JSON format:
                  {
                    "from_name": "<sender name>",
                    "from_company": "<optional company>",
                    "from_email": "<sender email>",
                    "subject": "<email subject>",
                    "body": "<email body>"
                  }
                """);
                default -> sb.append("- ").append(tool.getToolName()).append(": No description provided.\n");
            }
        }

        // Append context
        sb.append("\nContext:\n");
        sb.append(context);

        return sb.toString();
    }
}