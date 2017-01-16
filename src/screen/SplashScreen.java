package screen;

import custom_texture.GradientPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import objects.Trainer;

/**
 * @author Thomas
 */
public class SplashScreen extends JWindow {
    private static final JProgressBar PROGRESS_BAR = new JProgressBar();
    private static final int TIMER_PAUSE = 100, PROGBAR_MAX = 100;
    private int COUNT = 1;
    private final String screenName;
    private static Timer progressBarTimer;
    private Board gameFrame;
    private final ActionListener al = new ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            PROGRESS_BAR.setValue(COUNT);
            if (PROGBAR_MAX == COUNT || gameFrame.isShowing()) {
                progressBarTimer.stop();
                SplashScreen.this.setVisible(false);
                createAndShowFrame();
            }
            COUNT++;
        }
    };

    public SplashScreen(String name, Trainer mainTrainer) {
        Container container = getContentPane();
        screenName = name;
        
        try {
            gameFrame = new Board(screenName, mainTrainer);
        } catch (IOException ex) {
        }

        JPanel panel = new GradientPanel(Color.red, Color.white);
        panel.setBorder(new EtchedBorder());
        container.add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel("Pokèmon!");
        label.setForeground(Color.black);
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        panel.add(label);

        PROGRESS_BAR.setMaximum(PROGBAR_MAX);
        PROGRESS_BAR.setIndeterminate(true);
        PROGRESS_BAR.setForeground(Color.green);
        PROGRESS_BAR.setBackground(Color.gray);
        container.add(PROGRESS_BAR, BorderLayout.SOUTH);
        
        pack();
        centerFrame((JWindow) this);
        setVisible(true);
        startProgressBar();
    }

    private void startProgressBar() {
        progressBarTimer = new Timer(TIMER_PAUSE, al);
        progressBarTimer.start();
    }

    private void createAndShowFrame() {
        gameFrame.setVisible(true);
    }
    
    private void centerFrame(JWindow window) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - window.getWidth()/2;
        int dy = centerPoint.y - window.getHeight()/2;
        window.setLocation(dx, dy);
    }
}