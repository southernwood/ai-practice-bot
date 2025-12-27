package com.example.ai_practice_bot.tool.calendar;

import com.example.ai_practice_bot.dto.CreateCalendarEventRequest;
import com.example.ai_practice_bot.service.GoogleCalendarService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CalendarTool {

    private final GoogleCalendarService calendarService;

    public CalendarTool(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Tool(
            name = "create_calendar_event",
            description = "Create a calendar event in the user's primary Google Calendar"
    )
    public String createCalendarEvent(CreateCalendarEventRequest request) {
        try {
            return calendarService.createCalendarEvent(request);
        } catch (Exception e) {
            return "❌ Failed to create calendar event: " + e.getMessage();
        }
    }
}
