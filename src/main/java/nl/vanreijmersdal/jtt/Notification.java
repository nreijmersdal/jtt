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
    int x, y, offSetX, offSetY, yLocation;
    
    public Notification() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
            y = (int) rect.getMaxY();
            offSetY = -50;
            yLocation = -1;
        } else {
            y = 0;
            yLocation = 0;
            offSetY = 50;
        }
        x = (int) rect.getMaxX();
        offSetX = -25;
    }
    
    public void showNotification(String message, final boolean hide) {
        String html = "<html><body style='padding:15px;border: #ffffff solid 2px'>" + message + "</body></html>";
        
        JLabel text = new JLabel();
        text.setForeground(Color.WHITE);
        text.setText(html);

        final JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.getContentPane().add(text, BorderLayout.CENTER); 
        dialog.getContentPane().setBackground(Color.BLACK);
        dialog.setOpacity(0.80f);
        dialog.setAlwaysOnTop(true);
        dialog.pack();      

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
                if(hide) {
                    timer.stop();
                }
            }
        };
        dialog.addMouseListener(mouse);
        
        dialog.setLocation(x - dialog.getWidth() + offSetX, y + (dialog.getHeight() * yLocation) + offSetY);
        dialog.setVisible(true);
        if(hide) {      
            ActionListener action = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dialog.dispose();
                    timer.stop();
                }
            };
            timer = new Timer(5000, action);
            timer.start();
        }
    }
}
