package com.example.ai_practice_bot.controller;

import com.example.ai_practice_bot.dto.ChatRequest;
import com.example.ai_practice_bot.dto.ChatResponse;
import com.example.ai_practice_bot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatResponse reply = chatService.chat(request.userId(), request.message());
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong 3";
    }
}