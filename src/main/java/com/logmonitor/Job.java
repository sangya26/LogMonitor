package com.logmonitor;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Represents a job/task with PID, description, start/end times, duration, and status.
 * Handles duration calculation including overnight jobs and status thresholds.
 */
public class Job {
    private String pid;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private String status;

    private static final Duration WARNING_THRESHOLD = Duration.ofMinutes(5);
    private static final Duration ERROR_THRESHOLD = Duration.ofMinutes(10);

    // Constructor for active job (without endTime)
    public Job(String pid, String description, LocalTime startTime) {
        this.pid = pid;
        this.description = description;
        this.startTime = startTime;
    }

    // Complete the job with endTime and calculate duration & status
    public void complete(LocalTime endTime) {
        this.endTime = endTime;

        // Handle jobs spanning midnight
        if (endTime.isBefore(startTime)) {
            this.duration = Duration.between(startTime, LocalTime.MAX).plusSeconds(1)
                            .plus(Duration.between(LocalTime.MIN, endTime));
        } else {
            this.duration = Duration.between(startTime, endTime);
        }

        // Determine status based on thresholds
        if (duration.compareTo(ERROR_THRESHOLD) > 0) {
            status = "ERROR";
        } else if (duration.compareTo(WARNING_THRESHOLD) > 0) {
            status = "WARNING";
        } else {
            status = "OK";
        }
    }

    // Getters
    public String getPid() { return pid; }
    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Duration getDuration() { return duration; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        String dur = duration != null ? duration.toMinutesPart() + "m " + duration.toSecondsPart() + "s" : "N/A";
        return pid + " | " + description +
                " | Start: " + startTime +
                " | End: " + endTime +
                " | Duration: " + dur +
                " | Status: " + status;
    }
}
