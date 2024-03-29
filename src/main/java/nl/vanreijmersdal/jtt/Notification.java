package nl.vanreijmersdal.jtt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Replacement for default notifications
 */
public class Notification {
    Timer timer;
    int x, y, offSetX, offSetY, osLocation;
    
    public Notification() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
            // Show notifications bottom right
            y = (int) rect.getMaxY();
            offSetY = -50;
            osLocation = -1;
        } else { 
            // Other operating systems Mac OS X and Linux
            // Show notifications top right
            y = 0;
            osLocation = 0;
            offSetY = 50;
        }
        x = (int) rect.getMaxX();
        offSetX = -25;
    }
    
    public void showNotification(String message, int hideInXMilliSeconds) {
        String html = "<html>"
                    + " <body style='"
                    + "     padding:15px;"
                    + "     color: #fab80a;"
                    + " '>"
                    +       message
                    + " </body>"
                    + "</html>";
        
        JLabel text = new JLabel();
        text.setForeground(Color.WHITE);
        text.setText(html);

        final JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.getContentPane().add(text, BorderLayout.CENTER); 
        dialog.getContentPane().setBackground(Color.BLACK);
        dialog.setOpacity((float)0.80);
        dialog.setAlwaysOnTop(true);
        dialog.pack();      

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
                timer.stop();
                if(App.timeTracker.showWarning) {
                    App.timeTracker.startTask();
                }
            }
        };
        dialog.addMouseListener(mouse);
        
        dialog.setLocation(x - dialog.getWidth() + offSetX, y + (dialog.getHeight() * osLocation) + offSetY);
        dialog.setVisible(true);
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
                timer.stop();
                 if(App.timeTracker.showWarning) {
                    App.timeTracker.resetWarning();
                 }
           }
        };
        timer = new Timer(hideInXMilliSeconds, action);
        timer.start();
    }
}