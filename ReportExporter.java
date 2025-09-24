import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exports job reports in plain text and CSV formats
 */
public class ReportExporter {

    /**
     * Export job report as plain text
     * @param jobs List of completed jobs
     * @param filePath Output file path
     * @throws IOException
     */
    public static void exportText(List<Job> jobs, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Job job : jobs) {
                writer.write(job.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Export job report as CSV
     * @param jobs List of completed jobs
     * @param filePath Output CSV file path
     * @throws IOException
     */
    public static void exportCSV(List<Job> jobs, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("PID,Description,Start,End,Duration,Status\n");
            for (Job job : jobs) {
                writer.write(String.format("%s,%s,%s,%s,%dm %ds,%s\n",
                        job.getPid(),
                        job.getDescription(),
                        job.getStartTime(),
                        job.getEndTime(),
                        job.getDuration().toMinutesPart(),
                        job.getDuration().toSecondsPart(),
                        job.getStatus()));
            }
        }
    }
}
