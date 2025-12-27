package com.example.ai_practice_bot.dto;

import java.util.Map;

public record ToolIntent(
        String toolName,
        Map<String, Object> arguments
) {}
