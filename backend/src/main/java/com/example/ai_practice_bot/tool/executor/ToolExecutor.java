package com.example.ai_practice_bot.tool.executor;

public interface ToolExecutor {
    boolean supports(String toolName);
    String execute(Object request);
    String getToolName();
    String getToolDescription();
    boolean requiresConfirmation();
}
