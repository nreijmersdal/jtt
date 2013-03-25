jtt
===

Java Time Tracker

features
===
* logs time starting and spend on a task into a file
* append new sessions to the same file
* show notifications when you are not tracking time

roadmap
===
* idle monitoring
* pomodoro style break system
* retrieve todo/tasks from Jira

usage
===
* do a ```mvn clean install``` you will find a .jar in the target folder
* start it with ```java -jar timetracker.jar``` or ``java -jar timetracker.jar filename.log```
* right click the trayicon and click Start
time will now be logged in timetracker.log in the current directory

requirments
===
Java 1.7

tested on the following platforms
===
Windows 7
Ubuntu 13.04
Mac OS X 10.7
