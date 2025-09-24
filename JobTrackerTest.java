import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JobTrackerTest {

    /**
     * Test basic start/end of jobs and status calculation
     */
    @Test
    public void testJobCompletionAndStatus() {
        JobTracker tracker = new JobTracker();

        // Start jobs
        tracker.startJob("101", "Test Job 1", LocalTime.of(10, 0, 0));
        tracker.startJob("102", "Test Job 2", LocalTime.of(10, 5, 0));

        // End jobs
        tracker.endJob("101", LocalTime.of(10, 4, 0));  // 4 min => OK
        tracker.endJob("102", LocalTime.of(10, 12, 0)); // 7 min => WARNING

        List<Job> completed = tracker.getCompletedJobs();
        assertEquals(2, completed.size());

        Job job1 = completed.get(0);
        Job job2 = completed.get(1);

        assertEquals("OK", job1.getStatus());
        assertEquals("WARNING", job2.getStatus());
    }

    /**
     * Test jobs that span across midnight
     */
    @Test
    public void testJobDurationAcrossMidnight() {
        JobTracker tracker = new JobTracker();
        tracker.startJob("201", "Overnight Job", LocalTime.of(23, 58, 0));
        tracker.endJob("201", LocalTime.of(0, 3, 0)); // 5 min

        Job job = tracker.getCompletedJobs().get(0);
        assertEquals("WARNING", job.getStatus()); // 5 min = WARNING threshold
    }

    /**
     * Test end without a start event
     */
    @Test
    public void testEndWithoutStart() {
        JobTracker tracker = new JobTracker();
        tracker.endJob("999", LocalTime.of(12, 0, 0)); // Should print warning

        List<Job> completed = tracker.getCompletedJobs();
        assertEquals(0, completed.size());
    }

    /**
     * Test ReportExporter functionality (text & CSV)
     */
    @Test
    public void testReportExporterSimulation() throws Exception {
        JobTracker tracker = new JobTracker();
        tracker.startJob("301", "Export Job", LocalTime.of(9, 0, 0));
        tracker.endJob("301", LocalTime.of(9, 4, 0));

        List<Job> completed = tracker.getCompletedJobs();

        // Should not throw exceptions
        ReportExporter.exportText(completed, "test_report.txt");
        ReportExporter.exportCSV(completed, "test_report.csv");
    }

    /**
     * Optional: simulate multiple log events in-memory
     */
    @Test
    public void testSimulatedLogs() throws Exception {
        JobTracker tracker = new JobTracker();

        // Simulated log entries (timestamp, description, event, PID)
        String[][] logs = {
            {"10:00:00", "Job A", "START", "401"},
            {"10:03:00", "Job A", "END", "401"},
            {"10:05:00", "Job B", "START", "402"},
            {"10:16:00", "Job B", "END", "402"} // 11 min => ERROR
        };

        for (String[] log : logs) {
            LocalTime ts = LocalTime.parse(log[0]);
            String desc = log[1];
            String event = log[2];
            String pid = log[3];

            if (event.equalsIgnoreCase("START")) {
                tracker.startJob(pid, desc, ts);
            } else if (event.equalsIgnoreCase("END")) {
                tracker.endJob(pid, ts);
            }
        }

        List<Job> completed = tracker.getCompletedJobs();
        assertEquals(2, completed.size());
        assertEquals("OK", completed.get(0).getStatus());
        assertEquals("ERROR", completed.get(1).getStatus());
    }
}
