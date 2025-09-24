package com.logmonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Job, JobTracker, and ReportExporter.
 */
public class JobTrackerTest {

    @Test
    void testJobCompletionOK() {
        Job job = new Job("123", "Test Job", LocalTime.of(10, 0, 0));
        job.complete(LocalTime.of(10, 3, 0)); // 3 minutes
        assertEquals("OK", job.getStatus());
        assertEquals(Duration.ofMinutes(3), job.getDuration().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void testJobCompletionWarning() {
        Job job = new Job("124", "Long Job", LocalTime.of(10, 0, 0));
        job.complete(LocalTime.of(10, 7, 0)); // 7 minutes
        assertEquals("WARNING", job.getStatus());
    }

    @Test
    void testJobCompletionError() {
        Job job = new Job("125", "Too Long Job", LocalTime.of(10, 0, 0));
        job.complete(LocalTime.of(10, 15, 0)); // 15 minutes
        assertEquals("ERROR", job.getStatus());
    }

    @Test
    void testOvernightJob() {
        Job job = new Job("126", "Overnight Job", LocalTime.of(23, 55, 0));
        job.complete(LocalTime.of(0, 5, 0)); // Spans midnight
        Duration expected = Duration.ofMinutes(10);
        Duration actual = job.getDuration().truncatedTo(ChronoUnit.MINUTES);
        assertEquals(expected, actual);
    }

    @Test
    void testJobTrackerStartAndEnd() {
        JobTracker tracker = new JobTracker();
        tracker.startJob("200", "Tracker Job", LocalTime.of(12, 0, 0));
        tracker.endJob("200", LocalTime.of(12, 2, 0));

        List<Job> completed = tracker.getCompletedJobs();
        assertEquals(1, completed.size());
        assertEquals("OK", completed.get(0).getStatus());
    }

    @Test
    void testReportExporterWritesFiles() throws IOException {
        JobTracker tracker = new JobTracker();
        tracker.startJob("300", "Export Job", LocalTime.of(9, 0, 0));
        tracker.endJob("300", LocalTime.of(9, 6, 0));

        List<Job> completed = tracker.getCompletedJobs();

        // Export reports
        File txtFile = new File("test_report.log");
        File csvFile = new File("test_report.csv");

        ReportExporter.exportText(completed, txtFile.getAbsolutePath());
        ReportExporter.exportCSV(completed, csvFile.getAbsolutePath());

        assertTrue(txtFile.exists());
        assertTrue(csvFile.exists());

        // Basic validation content is written
        String txtContent = Files.readString(txtFile.toPath());
        String csvContent = Files.readString(csvFile.toPath());

        assertTrue(txtContent.contains("Export Job"));
        assertTrue(csvContent.contains("Export Job"));

        // Cleanup
        txtFile.delete();
        csvFile.delete();
    }
}
