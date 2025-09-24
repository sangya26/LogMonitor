
### Log Monitoring Application
A robust and modular Java application for monitoring and analyzing job logs. It parses log files, tracks job durations, evaluates processing time against thresholds, and generates reports in multiple formats for monitoring and integration purposes.

## Features
- Log Parsing: Reads log files with timestamps, job descriptions, event types (START/END), and PIDs.
- Job Tracking: Measures job duration and handles jobs spanning midnight.
- Status Evaluation: Automatically classifies job status based on thresholds:
    - OK – completed within 5 minutes
    - WARNING – duration exceeds 5 minutes
    - ERROR – duration exceeds 10 minutes
- Multiple Report Formats: Exports reports in:
    - Plain text (report.log)
    - CSV (report.csv) for integration with other systems
- Modular Design: Separate classes for better readability, maintainability, and testability:
    - Job.java – represents both active and completed jobs
    - JobTracker.java – manages active jobs and completes them
    - LogParser.java – parses logs and returns completed jobs
    - ReportExporter.java – handles exporting to text, CSV, and JSON
    - LogMonitor.java – main application orchestrator


## Prerequisites
Java 17 or later

## Getting Started
Clone the repository:
git clone [https://github.com/your-username/log-monitor.git](https://github.com/sangya26/LogMonitor)
cd LogMonitor
Place your log file (logs.log) in the project root.
Compile the Java files:
javac *.java
Run the application:
java LogMonitor

## Output
The application generates reports in the project root:
report.log → plain text summary
report.csv → CSV format for integration

Each report includes:
PID
Job description
Start time
End time
Duration (seconds)
Status (OK, WARNING, ERROR)
Log File Format
HH:MM:SS,job description,START|END,PID

Example:
11:35:23,scheduled task 032,START,37980
11:35:56,scheduled task 032,END,37980
Project Structure
log-monitor/
 ├─ Job.java
 ├─ JobTracker.java
 ├─ LogParser.java
 ├─ ReportExporter.java
 ├─ LogMonitor.java
 ├─ logs.log
 ├─ report.log
 ├─ report.csv
 ├─ report.json
 └─ README.md
 
Extensibility
Thresholds and file paths can be made configurable via CLI arguments.
Additional report formats (XML, Excel) can be integrated.
Unit tests with JUnit can be added to validate parsing, status computation, and overnight jobs.