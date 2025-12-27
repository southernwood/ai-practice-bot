package com.example.ai_practice_bot.tool.calendar;

import com.example.ai_practice_bot.dto.CreateCalendarEventRequest;
import com.example.ai_practice_bot.tool.executor.ToolExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CalendarToolExecutor implements ToolExecutor {
    private final CalendarTool calendarTool;

    public CalendarToolExecutor(CalendarTool calendarTool) {
        this.calendarTool = calendarTool;
    }

    @Override
    public boolean supports(String toolName) {
        if (toolName == null) return false;
        toolName = toolName.toLowerCase();
        return toolName.contains("calendar") && toolName.contains("event");
    }

    @Override
    public String execute(Object request) {
        ObjectMapper mapper = new ObjectMapper();
        CreateCalendarEventRequest req = mapper.convertValue(request, CreateCalendarEventRequest.class);

        return calendarTool.createCalendarEvent(req);
    }

    @Override
    public String getToolName() {
        return "create_calendar_event";
    }

    @Override
    public String getToolDescription() {
        return "Creates a meeting in my Google Calendar with title, start time, and duration.";
    }

    @Override
    public boolean requiresConfirmation() {
        return true;
    }
}
