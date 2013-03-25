package nl.vanreijmersdal.jtt;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Java Time Tracker
 */
public class App {
    static TimeTracker timeTracker = new TimeTracker();;
    
    public static void main( String[] args ) throws InterruptedException {
              
        timeTracker.startTray();
        
    }
}

class TimeTracker {
    
    boolean taskRunning = false;
    boolean showWarning = false;
    int showWarningTime = 4000;
    long idleSince = new Date().getTime();
    long startTime;
    String outputFilename = "timetracker.log";
    File file = new File(outputFilename);
    
    Image idleIcon, startIcon;
    TrayIcon trayIcon = null;
    Notification notify = new Notification();

    public void startTask() {
        if(taskRunning == false) {
            taskRunning = true;
            startTime = new Date().getTime(); // Writing the description is also part of the total timespend
            String task = JOptionPane.showInputDialog(null, "Task description:", "Start new task", JOptionPane.PLAIN_MESSAGE);
            if( task != null) {
                notify.showNotification("Started task: " + task, 3000);
                trayIcon.setImage(startIcon);
                BufferedWriter output;
                try {
                    output = new BufferedWriter(new FileWriter(file, true)); 
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    output.append(dateFormat.format(startTime) + "\t" + task);
                    output.close();
                } catch (IOException ex) {
                    //
                }
                if(showWarning) {
                    resetWarning();
                }
            } else {
                taskRunning = false;
            }
        }
    }

    public void stopTask() {
        if(taskRunning == true) {
            taskRunning = false;
            idleSince = new Date().getTime();
            trayIcon.setImage(idleIcon);
            BufferedWriter output;
            try {
                output = new BufferedWriter(new FileWriter(file, true));
                output.append("\t" + timeSpend(new Date().getTime() - startTime));
                output.newLine();
                output.close();
            } catch (IOException ex) {
                //
            }
        } else {
            notify.showNotification("No task was started yet.", 3000);
        }
    }
    
    public void startTray() throws InterruptedException {

        if (SystemTray.isSupported()) {
            
            final SystemTray tray = SystemTray.getSystemTray();

            Dimension trayIconSize = tray.getTrayIconSize();
 
            String OS = System.getProperty("os.name").toLowerCase();
            if(OS.indexOf("linux") >= 0) {
                idleIcon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("logo_22px.png"));
                startIcon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("task_22px.png"));
            } else { 
                idleIcon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("logo_16px.png"));
                startIcon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("task_16px.png"));
            }
                       
            trayIcon = new TrayIcon(idleIcon);
            
            PopupMenu popup = new PopupMenu();
            MenuItem start = new MenuItem("Start");
            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startTask();
                }
            });
            popup.add(start);
           
            MenuItem stop = new MenuItem("Stop");
            popup.add(stop);
            stop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopTask();
                }
            });
            
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopTask();
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
            popup.add(exit);

            trayIcon.setToolTip("Time Tracker");
            trayIcon.setPopupMenu(popup);
            
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("Could not start tray application: " + e);
            }
            
            while(true) {
                long currentTime = new Date().getTime();
                if(currentTime - idleSince > showWarningTime && !taskRunning && !showWarning) {
                  notify.showNotification("Click here to start working!", 30000);
                  showWarning = true;
                }
                Thread.sleep(100); // Prevents 100% CPU usage for while(true). TODO replace with wait()/notify() system?               
            }            
        } else {
            System.out.println("Could not start tray application");
        }
    }

    public void resetWarning() {
        showWarning = false;
        idleSince = new Date().getTime();
    }

    String timeSpend(long i) {
       String append = "";
       long minutes = i / 60000;
       long seconds = (i % 60000) / 1000;
       if(seconds < 10) {
           append = "0";
       }
       return minutes + "." + append + seconds;
    }
}
