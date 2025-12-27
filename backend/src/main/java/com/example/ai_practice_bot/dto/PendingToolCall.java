package com.example.ai_practice_bot.dto;

import java.util.Map;

public class PendingToolCall {

    private String toolName;
    private Map<String, Object> arguments;

    public PendingToolCall(String toolName, Map<String, Object> arguments) {
        this.toolName = toolName;
        this.arguments = arguments;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }
}
