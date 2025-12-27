package com.example.ai_practice_bot.service;

import com.example.ai_practice_bot.dto.CreateCalendarEventRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleCalendarService {

    private static final String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    private static final String REFRESH_TOKEN = System.getenv("GOOGLE_REFRESH_TOKEN");
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String FREEBUSY_URL = "https://www.googleapis.com/calendar/v3/freeBusy";
    private static final String EVENTS_URL =
            "https://www.googleapis.com/calendar/v3/calendars/primary/events";


    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getAccessToken() throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("refresh_token", REFRESH_TOKEN)
                .add("grant_type", "refresh_token")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get access token: " + response.body().string());
            }
            JsonNode json = objectMapper.readTree(response.body().string());
            return json.get("access_token").asText();
        }
    }

    public List<TimeRange> getBusyTime(OffsetDateTime timeMin, OffsetDateTime timeMax) throws IOException {
        String accessToken = getAccessToken();

        String timeMinStr = timeMin.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String timeMaxStr = timeMax.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // Build JSON body with timeZone
        String jsonBody = String.format(
                "{ \"timeMin\": \"%s\", \"timeMax\": \"%s\", \"timeZone\": \"%s\", \"items\": [{\"id\": \"primary\"}] }",
                timeMinStr, timeMaxStr, timeMin.getOffset().toString() // or use a named TZ like "America/Los_Angeles"
        );

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(FREEBUSY_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get free/busy info: " + response.body().string());
            }

            JsonNode json = objectMapper.readTree(response.body().string());
            JsonNode busyArray = json.at("/calendars/primary/busy");

            List<TimeRange> result = new ArrayList<>();
            for (JsonNode period : busyArray) {
                OffsetDateTime start = OffsetDateTime.parse(period.get("start").asText());
                OffsetDateTime end = OffsetDateTime.parse(period.get("end").asText());
                result.add(new TimeRange(start, end));
            }
            return result;
        }
    }

    public String createSimpleEvent(
            String title,
            OffsetDateTime start,
            OffsetDateTime end
    ) throws IOException {

        String accessToken = getAccessToken();

        String jsonBody = String.format(
                """
                {
                  "summary": "%s",
                  "start": {
                    "dateTime": "%s"
                  },
                  "end": {
                    "dateTime": "%s"
                  }
                }
                """,
                title,
                start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(EVENTS_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new IOException(
                        "Failed to create event: " + response.code() + " " + responseBody
                );
            }

            JsonNode json = objectMapper.readTree(responseBody);
            return json.get("id").asText();
        }
    }

    public String createCalendarEvent(CreateCalendarEventRequest request) throws IOException {
        OffsetDateTime start;

        try {
            start = OffsetDateTime.parse(request.startTimeIso());
        } catch (Exception e) {

            LocalDateTime localStart = LocalDateTime.parse(request.startTimeIso());
            start = localStart.atOffset(ZoneOffset.ofHours(-8));
        }

        if (request.targetTimeZone() != null && !request.targetTimeZone().isBlank()) {
            start = start.atZoneSameInstant(ZoneId.of(request.targetTimeZone())).toOffsetDateTime();
        }

        OffsetDateTime end = start.plusMinutes(request.durationMinutes());

        List<TimeRange> busyTimes = getBusyTime(start.minusMinutes(1), end.plusMinutes(1));
        for (TimeRange period : busyTimes) {
            if (start.isBefore(period.end) && end.isAfter(period.start)) {
                return String.format(
                        "⚠️ Cannot schedule '%s' at %s for %d minutes. Conflicts with existing event: %s → %s",
                        request.title(),
                        start,
                        request.durationMinutes(),
                        period.start,
                        period.end
                );
            }
        }

        String eventId = createSimpleEvent(request.title(), start, end);

        return String.format(
                "✅ Event '%s' scheduled at %s for %d minutes. Event ID: %s",
                request.title(),
                start,
                request.durationMinutes(),
                eventId
        );
    }



    public static class TimeRange {
        public OffsetDateTime start;
        public OffsetDateTime end;

        public TimeRange(OffsetDateTime start, OffsetDateTime end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start + " → " + end;
        }
    }

}
