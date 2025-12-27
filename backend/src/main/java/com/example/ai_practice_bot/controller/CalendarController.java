package com.example.ai_practice_bot.controller;

import com.example.ai_practice_bot.service.GoogleCalendarService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final GoogleCalendarService calendarService;

    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/test-event")
    public String createTestEvent() throws Exception {

        OffsetDateTime start = OffsetDateTime.now().plusHours(1);
        OffsetDateTime end = start.plusMinutes(30);

        String eventId = calendarService.createSimpleEvent(
                "🧪 Test Event via HTTP",
                start,
                end
        );

        return "Created event id = " + eventId;
    }
}