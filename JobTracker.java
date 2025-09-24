import java.time.LocalTime;
import java.util.*;

/**
 * Tracks active jobs and completes them when END events are encountered.
 */
public class JobTracker {
    private Map<String, Job> activeJobs = new HashMap<>();
    private List<Job> completedJobs = new ArrayList<>();

    /**
     * Start a new job
     * @param pid Unique process ID
     * @param description Job description
     * @param startTime Timestamp of START
     */
    public void startJob(String pid, String description, LocalTime startTime) {
        activeJobs.put(pid, new Job(pid, description, startTime));
    }

    /**
     * Complete a job
     * @param pid Unique process ID
     * @param endTime Timestamp of END
     */
    public void endJob(String pid, LocalTime endTime) {
        if (activeJobs.containsKey(pid)) {
            Job job = activeJobs.remove(pid);
            job.complete(endTime);
            completedJobs.add(job);
        } else {
            System.out.println("⚠️ END found without START for PID: " + pid);
        }
    }

    public List<Job> getCompletedJobs() {
        return completedJobs;
    }
}
