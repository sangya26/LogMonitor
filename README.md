
# Log Monitoring Application
A robust and modular Java application for monitoring and analyzing job logs. It parses log files, tracks job durations, evaluates processing time against thresholds, and generates reports in multiple formats for monitoring and integration purposes.

## Features

- Parse log files and extract job events.
- Track active jobs and compute execution duration.
- Handle overnight jobs (spanning across midnight).
- Assign job status:
    -  OK → Duration ≤ 5 minutes
    -  WARNING → 6–10 minutes
    -  ERROR → >10 minutes
- Export reports to:
    - Text (.log)
    - CSV (.csv)
- Unit tests with JUnit 5


## Prerequisites
- Java 11+
- Maven 3.6+

## Getting Started

- Clone the repository:
- git clone https://github.com/sangya26/LogMonitor
- cd LogMonitor
- Place your log file (logs.log) in the project root
- Build the project
- mvn clean install
- Run the application
- mvn compile exec:java 
- Run tests
- mvn test



## Example
- Input log file (logs.log):
```
11:36:58,background job wmy, START,81258
11:37:14,scheduled task 515, START,45135
```
- Generated report (report.log):
```
90962 | scheduled task 996 | Start: 11:40:51 | End: 11:42:46 | Duration: 1m 55s | Status: OK
90812 | background job dej | Start: 11:39:26 | End: 11:43:32 | Duration: 4m 6s | Status: OK
```
- Generated report (report.csv):
```
75164,scheduled task 173,11:45:47,11:46:51,1m 4s,OK
36709,background job djw,11:47:04,11:47:54,0m 50s,OK
```

![App Screenshot](images/Screenshot%202025-09-25%20at%2011.28.44.png)


## Project Structure
---------------------
```
log-monitor/
 ├─ src/
 │   ├─ main/java/com/logmonitor/
 │   │   ├─ Job.java
 │   │   ├─ JobTracker.java
 │   │   ├─ LogParser.java
 │   │   ├─ ReportExporter.java
 │   │   └─ LogMonitor.java   # Entry point
 │   └─ test/java/com/logmonitor/
 │       └─ JobTrackerTest.java
 ├─ pom.xml
 ├─ logs.log
 ├─ report.log
 ├─ report.csv
 └─ README.md
```

