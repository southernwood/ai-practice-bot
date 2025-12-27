package com.example.ai_practice_bot.tool.calendar;


import com.example.ai_practice_bot.service.GoogleCalendarService;
import com.example.ai_practice_bot.tool.executor.ToolExecutor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class FindFreeTimeExecutor implements ToolExecutor {

    private final FreeTimeTool freeTimeTool;

    public FindFreeTimeExecutor(GoogleCalendarService calendarService) {
        this.freeTimeTool = new FreeTimeTool(calendarService);
    }

    @Override
    public String getToolName() {
        return "find_free_time";
    }

    @Override
    public String getToolDescription() {
        return "Queries up to 3 free time slots in my Google Calendar during specified work hours.";
    }

    @Override
    public boolean requiresConfirmation() {
        // Querying free time does not modify anything, so no confirmation needed
        return false;
    }

    @Override
    public boolean supports(String toolName) {
        return getToolName().equals(toolName);
    }

    @Override
    public String execute(Object request) {
        try {
            // Expecting a Map<String, Object> as request
            if (!(request instanceof Map)) {
                return "Invalid request format. Expecting JSON-like arguments.";
            }

            Map<String, Object> arguments = (Map<String, Object>) request;

            String dateStr = (String) arguments.get("date"); // YYYY-MM-DD
            String workStartStr = (String) arguments.getOrDefault("workStart", "09:00");
            String workEndStr = (String) arguments.getOrDefault("workEnd", "18:00");

            // Construct OffsetDateTime for work start/end
            OffsetDateTime workStart = OffsetDateTime.parse(dateStr + "T" + workStartStr + ":00-08:00");
            OffsetDateTime workEnd = OffsetDateTime.parse(dateStr + "T" + workEndStr + ":00-08:00");

            // Get up to 3 free slots
            List<GoogleCalendarService.TimeRange> freeSlots = freeTimeTool.getFreeSlots(workStart, workEnd, 3);

            if (freeSlots.isEmpty()) {
                return "No free time slots of at least 1 hour were found on " + dateStr;
            }

            // Build response string
            StringBuilder sb = new StringBuilder();
            sb.append("Free time slots on ").append(dateStr).append(":\n");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            for (GoogleCalendarService.TimeRange slot : freeSlots) {
                sb.append(slot.start.toLocalTime().format(timeFormatter))
                        .append(" → ")
                        .append(slot.end.toLocalTime().format(timeFormatter))
                        .append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to find free time: " + e.getMessage();
        }
    }
}