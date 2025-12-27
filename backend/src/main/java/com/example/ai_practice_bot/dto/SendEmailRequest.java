package com.example.ai_practice_bot.dto;

public record SendEmailRequest (
        String fromName,
        String fromCompany,
        String fromEmail,
        String subject,
        String body
){}