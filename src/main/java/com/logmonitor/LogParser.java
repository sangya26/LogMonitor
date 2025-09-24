package com.logmonitor;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


//Parses a log file and feeds START and END events to JobTracker.

public class LogParser {
    private String filePath;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogParser(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Parse the log file and send events to JobTracker
     * @param tracker JobTracker instance
     * @throws IOException
     */
    public void parse(JobTracker tracker) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;

                LocalTime timestamp = LocalTime.parse(parts[0].trim(), TIME_FORMAT);
                String description = parts[1].trim();
                String event = parts[2].trim();
                String pid = parts[3].trim();

                if (event.equalsIgnoreCase("START")) {
                    tracker.startJob(pid, description, timestamp);
                } else if (event.equalsIgnoreCase("END")) {
                    tracker.endJob(pid, timestamp);
                }
            }
        }
    }
}
