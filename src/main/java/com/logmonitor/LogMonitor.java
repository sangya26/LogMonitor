package com.logmonitor;
import java.io.IOException;
import java.util.List;


//this class performs log parsing, job tracking, and report exporting.

public class LogMonitor {
    public static void main(String[] args) throws IOException{
        String logFile = args.length > 0 ? args[0] : "logs.log";
        String textReport = "report.log";
        String csvReport = "report.csv";

        JobTracker tracker = new JobTracker();
        LogParser parser = new LogParser(logFile);

        try {
            parser.parse(tracker);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<Job> completedJobs = tracker.getCompletedJobs();

        try {
            ReportExporter.exportText(completedJobs, textReport);
            ReportExporter.exportCSV(completedJobs, csvReport);
            System.out.println(" Reports generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
