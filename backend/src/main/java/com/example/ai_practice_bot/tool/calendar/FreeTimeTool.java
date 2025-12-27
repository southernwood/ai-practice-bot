package com.example.ai_practice_bot.tool.calendar;

import com.example.ai_practice_bot.service.GoogleCalendarService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class FreeTimeTool {

    // Minimum free duration in hours
    private static final int MIN_FREE_HOURS = 1;

    private final GoogleCalendarService calendarService;

    public FreeTimeTool(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Get free time slots within the specified work period
     *
     * @param workStart Start of work time
     * @param workEnd   End of work time
     * @param maxSlots  Maximum number of free slots to return
     * @return List of free time ranges
     * @throws Exception
     */
    public List<GoogleCalendarService.TimeRange> getFreeSlots(
            OffsetDateTime workStart,
            OffsetDateTime workEnd,
            int maxSlots
    ) throws Exception {

        // Retrieve busy time slots from Google Calendar
        List<GoogleCalendarService.TimeRange> busy = calendarService.getBusyTime(workStart, workEnd);

        // Sort busy slots by start time
        busy.sort(Comparator.comparing(tr -> tr.start));

        List<GoogleCalendarService.TimeRange> freeSlots = new ArrayList<>();

        OffsetDateTime current = workStart;

        // Iterate over busy slots and find gaps
        for (GoogleCalendarService.TimeRange b : busy) {
            if (b.start.isAfter(current)) {
                Duration duration = Duration.between(current, b.start);
                if (duration.toHours() >= MIN_FREE_HOURS) {
                    freeSlots.add(new GoogleCalendarService.TimeRange(current, b.start));
                    if (freeSlots.size() >= maxSlots) {
                        return freeSlots;
                    }
                }
            }
            if (b.end.isAfter(current)) {
                current = b.end;
            }
        }

        // Check the last gap from last busy slot to work end
        if (current.isBefore(workEnd)) {
            Duration duration = Duration.between(current, workEnd);
            if (duration.toHours() >= MIN_FREE_HOURS) {
                freeSlots.add(new GoogleCalendarService.TimeRange(current, workEnd));
            }
        }

        // If more than maxSlots, truncate the list
        if (freeSlots.size() > maxSlots) {
            return freeSlots.subList(0, maxSlots);
        }

        return freeSlots;
    }

    /**
     * Get all free slots within the specified work period
     *
     * @param workStart Start of work time
     * @param workEnd   End of work time
     * @return List of free time ranges
     * @throws Exception
     */
    public List<GoogleCalendarService.TimeRange> getFreeSlots(
            OffsetDateTime workStart,
            OffsetDateTime workEnd
    ) throws Exception {
        return getFreeSlots(workStart, workEnd, Integer.MAX_VALUE);
    }
}
