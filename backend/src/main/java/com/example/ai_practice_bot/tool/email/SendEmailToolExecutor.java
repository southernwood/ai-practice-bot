package com.example.ai_practice_bot.tool.email;

import com.example.ai_practice_bot.dto.SendEmailRequest;
import com.example.ai_practice_bot.tool.executor.ToolExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class SendEmailToolExecutor implements ToolExecutor {

    private final EmailTool emailTool;

    public SendEmailToolExecutor(EmailTool emailTool) {
        this.emailTool = emailTool;
    }

    @Override
    public boolean supports(String toolName) {
        if (toolName == null) return false;
        toolName = toolName.toLowerCase();
        return toolName.contains("send") && toolName.contains("email");
    }

    @Override
    public String execute(Object request) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SendEmailRequest req = mapper.convertValue(request, SendEmailRequest.class);
        return emailTool.sendEmail(req);
    }

    @Override
    public String getToolName() {
        return "send_email";
    }

    @Override
    public String getToolDescription() {
        return "Sends an email to me on behalf of a visitor.";
    }

    @Override
    public boolean requiresConfirmation() {
        return false;
    }
}