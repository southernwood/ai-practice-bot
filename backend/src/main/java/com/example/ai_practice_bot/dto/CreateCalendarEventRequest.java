package com.example.ai_practice_bot.dto;

public record CreateCalendarEventRequest(
        String title,
        String startTimeIso,
        Integer durationMinutes,
        String targetTimeZone
) {}