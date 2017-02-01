package custom_texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import screen.Board;

/**
 * @author Thomas
 */
public class ExpandPanel extends JPanel {
    public boolean lock;
    public Color border1, border2;
    public Color back1, back2;

    public ExpandPanel() {
        back2 = Color.gray;
        back1 = Color.gray;
        border2 = Color.gray;
        border1 = Color.gray;
        lock = false;
    }
    
    /**
     * 
     */
    public final void expandComponent() {
        int width = (int) (getMinimumSize().width*2*(Board.xDIM));
        int height = (int) (getMinimumSize().height*2*(Board.yDIM));
        setPreferredSize(new Dimension(width, height));
        for (Object obj : getComponents()) {
            if (obj instanceof Container) {
                Container panel = (Container) obj;
                containerIterator(panel, true);
            }
        }
    }
    
    /**
     * 
     * @param cont
     * @param font 
     */
    public void containerIterator(Container cont, boolean font) {
        for (Component comp : cont.getComponents()) {
            if (font) {
                double fontSize = (Board.xDIM) < (Board.yDIM) ? (Board.xDIM) : (Board.yDIM);
                comp.setFont(new Font("Trebuchet MS", 0, (int) ((11*fontSize)+(1*fontSize))));
            }
            if (comp instanceof Container) {
                Container panel = (Container) comp;
                containerIterator(panel, font);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getIcon() != null) {
                    if (label.getWidth() != label.getHeight()) {
                        int width = (int) (label.getMinimumSize().width*2*(Board.xDIM));
                        int height = (int) (label.getMinimumSize().height*2*(Board.yDIM));
                        label.setIcon(ImageConverter.getScaledImage(
                                ImageConverter.iconToImageIcon(label.getIcon()), width, height));
                    } else {
                        label.setIcon(ImageConverter.getScaledImage(
                                ImageConverter.iconToImageIcon(label.getIcon()),
                                (int) (label.getWidth()*(Board.xDIM)),
                                (int) (label.getHeight()*(Board.yDIM))));
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param max
     * @param min
     * @param h
     * @param clock
     * @param part 
     */
    public void generateParticle(final int max, final int min, boolean h, int clock, final Particle part) {
        Timer timer = new Timer(clock, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Random r = new Random();
                int pos = r.nextInt((max - min) + 1) + min;
//                Particle p = new Particle();
//                add(p);
            }
        });
    }
    
    public void changeGraphic() {
        
    }
    
    public void checkKey(KeyEvent e) {
        
    }
}