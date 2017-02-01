package custom_texture;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Thomas
 */
public class FadingText extends JLabel {
    private static final int CLOCK = 50;
    private int alpha = 255;
    private int increment = 5;
    public int r = 0, g = 0, b = 0;
    
    public FadingText(boolean out, final JPanel frame) {
        if (out) {
            new Timer(CLOCK, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha -= increment;
                    if (alpha <= 0) {
                        alpha = 0;
                        if (frame != null) {
                            remove(frame);
                        }
                    }
                    setForeground(new Color(r, g, b, alpha));
//                    setBackground(new Color(r, g, b, alpha));
//                    repaint(); revalidate();
                }
            }).start();
        } else {
            new Timer(CLOCK, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha += increment;
                    if (alpha <= 255) {
                        alpha = 0;
                        if (frame != null) {
                            remove(frame);
                        }
                    }
                    setForeground(new Color(r, g, b, alpha));
//                    setBackground(new Color(r, g, b, alpha));
//                    repaint(); revalidate();
                }
            }).start();
        }     
    }
    
    public int getAlpha() {
        return alpha;
    }
    
    private void remove(JPanel frame) {
       frame.remove(this);
    }
}
